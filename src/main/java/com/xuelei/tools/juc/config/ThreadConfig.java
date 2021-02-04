package com.xuelei.tools.juc.config;


import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ThreadConfig {

    @Bean
    public ThreadPoolTaskExecutor getPool(){
        // ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setBeanName("task-async");
        return threadPoolTaskExecutor;
    }
}
