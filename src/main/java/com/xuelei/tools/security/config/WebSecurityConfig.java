package com.xuelei.tools.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.authorization.DelegatingReactiveAuthorizationManager;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcherEntry;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebFluxSecurity //启用security安全框架
@EnableReactiveMethodSecurity //启用@PreAuthorize注解配置，如果不加则注解不会生效
public class WebSecurityConfig{

    //可换为redis存储
    private final Map<String, SecurityContext> tokenCache = new ConcurrentHashMap<>();

    private static final String BEARER = "Bearer ";

    private static final String[] AUTH_WHITELIST = new String[]{"/login", "/tools/actuator/**", "/tools/ucenter-web/login"};

    public static final String PASSWD = "123456";//固定密码

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http.cors().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()).and()
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)//认证
                .addFilterAt(accessWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)//动态鉴权
                .authorizeExchange().pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated();
        return http.build();
    }

    @Bean
    AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authenticationWebFilter.setRequiresAuthenticationMatcher(new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(AUTH_WHITELIST)));
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter());
        authenticationWebFilter.setSecurityContextRepository(serverSecurityContextRepository());
        authenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(serverAuthenticationEntryPoint()));
        return authenticationWebFilter;
    }

    /**
     * 认证管理
     * @date 2019/11/19
     */
    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        final ReactiveUserDetailsService detailsService = userDetailsService();
        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
        managers.add(authentication -> {
            //其他登陆方式(比如手机号验证码登陆)可在此设置不得抛出异常或者Mono.error
            return Mono.empty();
        });
        //必须放最后不然会优先使用用户名密码校验但是用户名密码不对时此AuthenticationManager会调用Mono.error造成后面的AuthenticationManager不生效
        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(detailsService));
        return new DelegatingReactiveAuthenticationManager(managers);
    }

    @Bean
    ServerAuthenticationEntryPoint serverAuthenticationEntryPoint() {

        return (exchange, e) -> {
            return Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> {
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        DataBufferFactory dataBufferFactory = response.bufferFactory();
                        DataBuffer buffer = dataBufferFactory.wrap(String.format("{\"msg\" : \"%s\"}", HttpStatus.UNAUTHORIZED.getReasonPhrase()).getBytes(
                                Charset.defaultCharset()));
                        return response.writeWith(Mono.just(buffer))
                                .doOnError(error -> DataBufferUtils.release(buffer));
                    });
        };
    }

    @Bean
    ServerAuthenticationConverter serverAuthenticationConverter() {
        final AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        return exchange -> {

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (StringUtils.isEmpty(token)) {
                return Mono.just(anonymous);
            }

            if (!token.startsWith(BEARER) || token.length() <= BEARER.length() /*|| !tokenCache.containsKey(token.substring(BEARER.length()))*/) {
                return Mono.just(anonymous);
            }

            // TODO 假装jwt token解析成功

//            String authenticationStr = (String) redisTemplate.opsForValue().get(token.substring(BEARER.length()));
//            if (StringUtils.isEmpty(authenticationStr)) {
//                return Mono.just(anonymous);
//            }
//            UsernamePasswordAuthenticationToken authentication = JSONUtil.parseObj(authenticationStr).toBean(UsernamePasswordAuthenticationToken.class);
//            return Mono.just(anonymous);
            // TODO 假装用户返回成功
            return Mono.just(new UsernamePasswordAuthenticationToken("zgc","123456", Collections.singletonList(new SimpleGrantedAuthority("user_base"))));


        };
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    DataBuffer buffer = dataBufferFactory.wrap(String.format("{\"msg\" : \"%s\"}", HttpStatus.FORBIDDEN.getReasonPhrase()).getBytes(
                            Charset.defaultCharset()));
                    return response.writeWith(Mono.just(buffer))
                            .doOnError(error -> DataBufferUtils.release(buffer));
                });
    }

    @Bean
    WebFilter accessWebFilter() {

        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().pathWithinApplication().value();

            ServerWebExchangeMatchers.pathMatchers(AUTH_WHITELIST)
                    .matches(exchange)
                    .map(ServerWebExchangeMatcher.MatchResult::isMatch)
                    .subscribe(IgnoreAuthWhitelist::set);

            //排除.pathMatchers(AUTH_WHITELIST).permitAll()
            if (IgnoreAuthWhitelist.get()) {
                return chain.filter(exchange);
            }

            DelegatingReactiveAuthorizationManager.Builder builder = DelegatingReactiveAuthorizationManager.builder();

            AuthorityReactiveAuthorizationManager authorityManager = AuthorityReactiveAuthorizationManager.hasAuthority(path);

            //参考 org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec.Access
            builder.add(new ServerWebExchangeMatcherEntry<>(
                    ServerWebExchangeMatchers.pathMatchers(path), authorityManager));

            DelegatingReactiveAuthorizationManager manager = builder.build();

            //参考 org.springframework.security.web.server.authorization.AuthorizationWebFilter.filter
            return ReactiveSecurityContextHolder.getContext()
                    .filter(c -> c.getAuthentication() != null)
                    .map(SecurityContext::getAuthentication)
                    .as(authentication -> manager.verify(authentication, exchange))
                    .switchIfEmpty(chain.filter(exchange));
        };
    }

    @Bean
    ServerSecurityContextRepository serverSecurityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    /**
     * 创建内存用户
     * @return 内存用户操作服务
     */
    @Bean
    ReactiveUserDetailsService userDetailsService() {

        // https://www.ctolib.com/docs-Java-Functional-Programming-c--235768.html search 词法作用域
        //final RedisTemplate redisTemplateFinal = this.redisTemplate;//java函数式编程中需要final修饰
        return username -> {
			/*
				正常步骤 根据username从数据库查询对应的user
				因为这边是给ucenter-web认证 只有正确的情况才会存储认证信息所以可以不校验密码信息(这里使用固定密码)
			 */
            List<String> strings = Arrays.asList("test", "test1", "test2");
//            List<String> strings = Arrays.asList("/loginRob", "/getAuthorities", "/ucenter-web/oneself/permissions");

            //redis 获取用户所有的权限
//            List<String> range = redisTemplate.opsForList().range(username, 0, -1);//redis序列化的原因 误认为是final修饰的问题
//            strings.addAll(range);

            String[] authorityArray = strings.toArray(new String[]{});
//            log.info(" username : {} ", username);

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            User.UserBuilder userBuilder = User.builder();
            UserDetails result = userBuilder.username(username).password(PASSWD).authorities(authorityArray).passwordEncoder(encoder::encode).build();
            return result == null ? Mono.empty() : Mono.just(User.withUserDetails(result).build());
        };
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    public static class IgnoreAuthWhitelist {

        private static ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();

        private IgnoreAuthWhitelist() {
        }

        public static void set(boolean auth) {
            threadLocal.set(auth);
        }

        public static boolean get() {
            return threadLocal.get();
        }

    }

}
