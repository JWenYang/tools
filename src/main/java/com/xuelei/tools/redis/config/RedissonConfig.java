package com.xuelei.tools.redis.config;

import com.xuelei.tools.redis.util.RedisUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RedissonConfig {

    private final static Logger log = LoggerFactory.getLogger(RedissonConfig.class);

    @Value("${spring.redis.sentinel.nodes}")
    List<String> address;

    @Value("${spring.redis.database}")
    int datebase;

    @Value("${spring.redis.password}")
    String password;

    @Value("${spring.redis.sentinel.master}")
    String master;

    @Bean
    public RedissonClient getRedission(){
        Config config = new Config();
        config.useSentinelServers()
                .addSentinelAddress(address.stream().map(ar->"redis://".concat(ar)).collect(Collectors.toList()).toArray(new String[0]))
                .setReadMode(ReadMode.SLAVE)
                .setDatabase(datebase)
                .setMasterName(master)
                .setPassword(password)
                .setMasterConnectionPoolSize(5)
                .setMasterConnectionMinimumIdleSize(5)
                .setSlaveConnectionMinimumIdleSize(5)
                .setSlaveConnectionPoolSize(5)
                .setTimeout(5000);
        RedissonClient redissonClient = Redisson.create(config);
        if(null != redissonClient){
            log.debug("redisson loading complete");
        }
        return redissonClient;
    }
}
