package com.xuelei.tools.juc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
public class ThreadService {

    private final static Logger log = LoggerFactory.getLogger(ThreadService.class);

    private final ReentrantLock reentrantLock = new ReentrantLock(false);

    private volatile int a = 0;

    public int getA() {
        return a;
    }

    public void reset() {
        this.a = 0;
    }

    public void setA(int a) {
        //1550
//        for (int i = 0; i < 50; i++) {
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            synchronized (this){
//                this.a += a;
//                log.info("{},{}", Thread.currentThread().getName(),this.a);
//            }
//        }
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(20);
                reentrantLock.lock();
                this.a += a;
                log.debug("{},{}", Thread.currentThread().getName(), this.a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
