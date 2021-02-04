package com.xuelei.tools.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private final static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    private static RedisTemplate redisTemplate;

    @Autowired
    private void RedisUtils(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public static void setValue(String key, Map<String, Object> value ,long timeout ,TimeUnit timeUnit) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public static Object getValue(String key) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public static void setValue(String key, String value ,long timeout ,TimeUnit timeUnit) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public static void setValue(String key, Object value ,long timeout ,TimeUnit timeUnit) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public static Object getMapValue(String key) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

}
