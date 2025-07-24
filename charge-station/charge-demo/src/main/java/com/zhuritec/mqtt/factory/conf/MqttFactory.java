package com.zhuritec.mqtt.factory.conf;

import com.zhuritec.mqtt.factory.MqttConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.ConsumerStopAction;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Slf4j
@EnableIntegration
@Configuration
public class MqttFactory {
    @Resource
    private Mqttconfig mqttconfig;

    public DefaultMqttPahoClientFactory getMqttFactory() {
        DefaultMqttPahoClientFactory factory=new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = mqttconfig.getOptions();
        options.setServerURIs(new String[]{
                mqttconfig.getHost()
        });
        factory.setConnectionOptions(options);
        return factory;

    }

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
    @ServiceActivator(outputChannel = MqttConstant.OUTBOUND_CHANNEL_NAME)
    public MqttPahoMessageHandler outHandler(){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttconfig.getClientId(), getMqttFactory());

        return messageHandler;
    }



    /**
     * =====================接收消息的步骤==========================
     * 1.创建接收通道
     * 2.创建消息处理器，并绑定接收通道（使用ServiceActivator注解）
     * 3.创建订阅主题的适配器
     *============================================================
     */

    // 接收通道
    @Bean(name= MqttConstant.INBOUND_CHANNEL_NAME)
    public MessageChannel getInboundChannel(){
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = MqttConstant.INBOUND_CHANNEL_NAME)
    public MessageHandler inHandler(){
        return (message)->
            log.info("收到消息：{}",message.getPayload().toString());
    }


    /**
     * 配置订阅主题的适配器
     * @return
     */
    @Bean
    public MessageProducer getAdapter(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttconfig.getClientId(), getMqttFactory(), mqttconfig.getTopic());

        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(getInboundChannel());
        return adapter;
    }
}
