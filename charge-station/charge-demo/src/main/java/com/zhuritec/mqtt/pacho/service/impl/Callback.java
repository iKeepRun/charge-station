package com.zhuritec.mqtt.pacho.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class Callback implements MqttCallback {
    @Override
    public void connectionLost(Throwable throwable){
       log.info("连接断开");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("收到消息:{}",mqttMessage.getPayload().toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            log.info("消息发送结果:{}",iMqttDeliveryToken.isComplete());
    }
}
