package com.zhuritec.factory;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class InBoundhandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

    }
}
