package com.xuelei.tools.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 */
@Component
public class RealObserver implements Observer {

    final static Logger log = LoggerFactory.getLogger(RealObserver.class);

    @Override
    public void update(Observable observable, Object o) {
        log.info("object:{}",o);
    }
}
