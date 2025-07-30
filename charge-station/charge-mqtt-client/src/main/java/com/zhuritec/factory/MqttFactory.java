package com.zhuritec.factory;


import com.zhuritec.conf.Mqttconfig;
import com.zhuritec.model.MqttConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;


/**
 * 注解EnableIntegration作用是能够扫描到@ServiceActivator注解的方法
 */
@Slf4j
//@EnableIntegration
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
        factory.setConnectionOptions(options);
        return factory;

    }










}
