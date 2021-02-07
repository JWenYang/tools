package com.xuelei.tools.juc.controller;

import com.xuelei.tools.juc.service.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.*;

@RestController
@RequestMapping("thread")
public class ThreadController {

    private final static Logger log = LoggerFactory.getLogger(ThreadController.class);

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ThreadService threadService;

    @GetMapping("thread")
    public Mono<ResponseEntity> thread(){

        //CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        long s = System.currentTimeMillis();
        threadService.reset();
        executor.execute(() -> {
            threadService.setA(1);
            if(null != countDownLatch){
                countDownLatch.countDown();
            }
        });
        executor.execute(() -> {
            threadService.setA(1);
            if(null != countDownLatch){
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("date of userd :{}:{}",System.currentTimeMillis()-s,threadService.getA());

        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
    }

    @GetMapping("thread0")
    public Mono<ResponseEntity> thread0() {

        long s = System.currentTimeMillis();
        threadService.reset();
        threadService.setA(1);
        threadService.setA(1);
        log.info("date of userd :{}", System.currentTimeMillis() - s);

        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
    }
}
