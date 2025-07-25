package com.zhuritec.controller;

import com.zhuritec.mqtt.factory.service.MqttService;
import com.zhuritec.mqtt.pacho.service.ClientService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttController {
//    @Resource
//    private ClientService clientService;
    @Resource
    private MqttService mqttService;
//    @RequestMapping("/pub")
//    public void publish(String topic,String message) {
//        clientService.publish(topic,message);
//    }
//
//    @RequestMapping("/sub")
//    public void sub(String topic) {
//        clientService.subscribe(topic);
//    }

    @RequestMapping("/factory/pub")
    public void factoryPublish(String topic,String message) {
        mqttService.sendToMqtt(topic,message);
    }

//    @RequestMapping("/factory/sub")
//    public void factorySub(String topic) {
//        mqttService.subscribe(topic);
//    }
}
