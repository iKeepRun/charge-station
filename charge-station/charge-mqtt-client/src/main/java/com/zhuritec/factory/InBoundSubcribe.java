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
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * 注解EnableIntegration作用是能够扫描到@ServiceActivator注解的方法
 */
@Slf4j
@EnableIntegration
@Configuration
public class InBoundSubcribe {
    @Resource
    private Mqttconfig mqttconfig;

    @Resource
    private MqttPahoClientFactory factory;

    @Resource
    private InBoundhandler inBoundhandler;
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




    /**
     * 配置接收消息的主题（这里用于接收充电桩状态数据）
     * 配置订阅主题的适配器(接收消息)
     * @return
     */
    @Bean
    public MessageProducer getAdapter(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttconfig.getClientId(), factory, mqttconfig.getTopic());

        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(getInboundChannel());
        return adapter;
    }

    /**
     *   接收消息处理器
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConstant.INBOUND_CHANNEL_NAME)
    public MessageHandler inHandler(){
        return inBoundhandler;
    }

}
