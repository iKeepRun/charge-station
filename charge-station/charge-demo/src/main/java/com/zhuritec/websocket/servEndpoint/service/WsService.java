package com.zhuritec.websocket.servEndpoint.service;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
//@ServerEndpoint("/ws/server")
@Component
public class WsService {

    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(String message){
        log.info("收到客户端消息:",message);
    }

    @OnOpen
    public void onOpen(){

        log.info("连接成功");
    }

    @OnClose
    public void onClose(){
        log.info("连接关闭");
    }

    @OnError
    public void onError(Throwable error){
        log.error("发生错误",error);
    }
}
