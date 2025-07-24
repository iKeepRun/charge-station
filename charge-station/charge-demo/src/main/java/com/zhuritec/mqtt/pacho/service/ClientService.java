package com.zhuritec.mqtt.pacho.service;

public interface ClientService {
    /**
     * 发布消息
     * @param topic
     * @param message
     */
    void publish(String topic, String message);
    /**
     * 订阅消息
     * @param topic
     */
    void subscribe(String topic);
    /**
     * 断开连接
     */
    void disconnect();
}
