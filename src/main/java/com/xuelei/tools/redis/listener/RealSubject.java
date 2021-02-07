package com.xuelei.tools.redis.listener;

import org.springframework.stereotype.Component;

import java.util.Observable;

/**
 * 观察主题
 */
@Component
public class RealSubject extends Observable {

    public void makeChange(Object o){
        //变化
        setChanged();
        //通知
        notifyObservers(o);
    }
}
