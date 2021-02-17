package com.xuelei.tools.filter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class BaseFilter implements WebFilter {

    private final static Logger log = LoggerFactory.getLogger(BaseFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        RequestPath path = serverWebExchange.getRequest().getPath();
        log.info(path.toString());
        return webFilterChain.filter(serverWebExchange);
        // return Mono.empty();
    }
}
