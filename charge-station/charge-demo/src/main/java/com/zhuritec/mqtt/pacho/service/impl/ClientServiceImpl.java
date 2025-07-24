package com.zhuritec.mqtt.pacho.service.impl;

import com.zhuritec.mqtt.pacho.service.ClientService;
import com.zhuritec.mqtt.pacho.util.MqttUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    @Resource
    private MqttUtil mqttUtil;
    @Override
    public void publish(String topic, String message) {
        mqttUtil.publish(topic, message);
    }

    @Override
    public void subscribe(String topic) {
       mqttUtil.subscribe(topic);
    }

    @Override
    public void disconnect() {
        mqttUtil.disconnect();
    }
}
