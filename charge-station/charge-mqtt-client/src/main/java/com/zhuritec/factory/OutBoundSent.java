package com.zhuritec.factory;

import com.zhuritec.conf.Mqttconfig;
import com.zhuritec.model.MqttConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
@Slf4j
public class OutBoundSent {
     @Resource
     private Mqttconfig mqttconfig;

     @Resource
     private MqttPahoClientFactory factory;
    /**
     * =====================发送消息的步骤==========================
     * 1.创建发送通道
     * 2.创建消息处理器，并绑定发送通道（使用ServiceActivator注解）
     * 3.将消息发送到发送通道(使用MessagingGateway注解)
     *============================================================
     */

    // 发送通道
    @Bean(name= MqttConstant.OUTBOUND_CHANNEL_NAME)
    public MessageChannel getOutboundChannel(){
        return new DirectChannel();
    }



    /**  发送通道的消息处理器
     * ServiceActivator注解，表示这个方法是一个消息处理器，会自动接收指定通道中的消息进行处理
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConstant.OUTBOUND_CHANNEL_NAME)
    public MqttPahoMessageHandler outHandler(DirectChannel errorChannel){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttconfig.getClientId(), factory);
        log.info("发送消息到：{}",messageHandler.getConnectionInfo());
        return messageHandler;
    }
}
