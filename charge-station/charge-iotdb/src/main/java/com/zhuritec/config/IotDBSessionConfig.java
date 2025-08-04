package com.zhuritec.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class IotDBSessionConfig {
    @Value("${iotdb.host}")
    private String host;
    @Value("${iotdb.port}")
    private int port;
    @Value("${iotdb.username}")
    private String username;
    @Value("${iotdb.password}")
    private String password;
    @Value("${iotdb.maxSize}")
    private int maxSize;

    private  static SessionPool sessionPool;

    @Bean
    public SessionPool initPool(){
        if(sessionPool==null){
            log.info("====初始化iotdb session pool");
            sessionPool = new SessionPool(host, port, username, password, maxSize);

        }
        return sessionPool ;
    }
}
