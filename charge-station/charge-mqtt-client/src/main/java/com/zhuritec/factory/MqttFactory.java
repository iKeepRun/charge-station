package com.zhuritec.factory;


import com.zhuritec.conf.Mqttconfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import java.nio.charset.StandardCharsets;


/**
 * 注解EnableIntegration作用是能够扫描到@ServiceActivator注解的方法
 */
@Slf4j
@EnableIntegration
@Configuration
public class MqttFactory {
    @Resource
    private Mqttconfig mqttconfig;

    @Bean
    public MqttPahoClientFactory getMqttFactory() {
        DefaultMqttPahoClientFactory factory=new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = mqttconfig.getOptions();
        options.setServerURIs(new String[]{
                mqttconfig.getHost()
        });
        //设置遗嘱消息，从配置文件中读取设备id
        options.setWill("mqtt/willMsg",
                new String("offline").getBytes(StandardCharsets.UTF_8),
                2,
                true);

        options.setCleanSession(false);    //保持会话
        options.setAutomaticReconnect(true); //自动重连
        options.setKeepAliveInterval(60);     //心跳时间
        options.setConnectionTimeout(30);     //连接超时时间


        factory.setConnectionOptions(options);


        return factory;

    }










}
