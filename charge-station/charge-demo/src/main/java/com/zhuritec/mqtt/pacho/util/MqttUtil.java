package com.zhuritec.mqtt.pacho.util;

import com.zhuritec.mqtt.pacho.conf.MqttConfig;
import com.zhuritec.mqtt.pacho.service.impl.Callback;
import com.zhuritec.mqtt.pacho.service.impl.MqttMessageListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class MqttUtil {
    @Resource
    private MqttConfig mqttConfig;
    @Resource
    private MqttMessageListener mqttMessageListener;

    private MqttClient client;

    @PostConstruct
    public void init() throws InterruptedException {
//        createClient();
//
//        connect();
    }

    private void createClient() {
        try {
            if(client==null){
                client = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientId());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic,String message) {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(message.getBytes());
        try {
            client.publish(topic,msg);
            log.info("消息发送成功");
        } catch (MqttException e) {
            log.error("消息发送失败:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic,mqttMessageListener);
             log.info("订阅成功:{}",topic);
        } catch (MqttException e) {
            log.error("订阅失败:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            if(client.isConnected())
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
                }
    }


    public void connect() {
        try {
            if(!client.isConnected()){
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(mqttConfig.getUsername());
                options.setPassword(mqttConfig.getPassword().toCharArray());
                options.setCleanSession(false);

                client.connect(options);
                client.setCallback(new Callback());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
