package com.zhuritec;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import com.zhuritec.mqtt.factory.conf.Mqttconfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = Mqttconfig.class)
public class MqttApplication {
    public static void main(String[] args) {
       SpringApplication.run(MqttApplication.class, args);
    }
}