package com.zhuritec.websocket.springanno.conf;

import com.zhuritec.websocket.springanno.handler.MyWSHandler;
import com.zhuritec.websocket.springanno.inteceptor.MyInteceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class MyWebsocketConfig implements WebSocketConfigurer {
    @Resource
    MyInteceptor inteceptor;
    @Resource
    MyWSHandler myHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){

        registry.addHandler(myHandler,"/ws/server").addInterceptors(inteceptor).setAllowedOrigins("*");
    }
}

