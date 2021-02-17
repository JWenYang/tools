package com.xuelei.tools.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
//public class SecurityContextRepository implements ServerSecurityContextRepository {
//
//    private final JwtAuthenticationManager jwtAuthenticationManager;
//
//
//    @Override
//    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Mono<SecurityContext> load(ServerWebExchange swe) {
//        log.info("访问 ServerSecurityContextRepository  。。。。。。。。。。。");
//
//        String token = exchange.getAttribute("token");
//        return jwtAuthenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(token, token)
//        )
//                .map(SecurityContextImpl::new);
//    }
//
//}

