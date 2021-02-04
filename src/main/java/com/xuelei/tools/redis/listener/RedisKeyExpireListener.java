package com.xuelei.tools.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener{

    private final static Logger log = LoggerFactory.getLogger(RedisKeyExpireListener.class);

    @Autowired
    private RealSubject realSubject;

    @Autowired
    private RealObserver realObserver;

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doRegister(RedisMessageListenerContainer listenerContainer) {
        listenerContainer.addMessageListener(this,new PatternTopic("__keyevent@*__:expired"));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //super.onMessage(message, pattern);
        log.info("expire key : {} {}",message.toString(),new String(pattern, Charset.forName("utf-8")));
        realSubject.addObserver(realObserver);
        realSubject.makeChange(message.toString());

    }
}
