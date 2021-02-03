package com.xuelei.tools.redis.controller;

import com.xuelei.tools.redis.util.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private static Logger log = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/set")
    public Mono<ResponseEntity> set() throws InterruptedException {
        RLock lock = redissonClient.getLock("20210202-abd");
        if(!lock.tryLock(2,120, TimeUnit.SECONDS)){
            return Mono.just(new ResponseEntity<>("请勿重复提交",HttpStatus.OK));
        }else{
            lock.lock(120, TimeUnit.SECONDS);
            log.debug("1111111111111");
            RedisUtil.setValue("20210202-abc","12212121",120, TimeUnit.SECONDS);
            return Mono.just(new ResponseEntity<>(RedisUtil.getValue("20210202-abc"),HttpStatus.OK));
        }

 //       lock.unlock();

    }

    @GetMapping("/set0")
    public Mono<ResponseEntity> set0() throws InterruptedException {
        RLock lock = redissonClient.getLock("20210202-abd");
        if(!lock.tryLock(2,120, TimeUnit.SECONDS)){
            return Mono.just(new ResponseEntity<>("请勿重复提交",HttpStatus.OK));
        }else{
            //lock.lock(120, TimeUnit.SECONDS);
            log.debug("1111111111111");
            RedisUtil.setValue("20210202-abc","12212121",120, TimeUnit.SECONDS);
            return Mono.just(new ResponseEntity<>(RedisUtil.getValue("20210202-abc"),HttpStatus.OK));
        }

        //       lock.unlock();

    }
}
