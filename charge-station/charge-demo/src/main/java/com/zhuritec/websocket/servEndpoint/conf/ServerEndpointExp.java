package com.zhuritec.websocket.servEndpoint.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 申明ServerEndpointExporter Bean
 * 作用： 自动注册使用了@ServerEndpoint注解的Bean， 不用手动调用registerEndpoint方法
 */
@Configuration
public class ServerEndpointExp {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return  new ServerEndpointExporter();
    }
}
