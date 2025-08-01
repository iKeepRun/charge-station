package com.zhuritec;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import com.zhuritec.conf.Mqttconfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.zhuritec.micro")
@SpringBootApplication
@EnableConfigurationProperties(value = Mqttconfig.class)
public class MqttApplication {
    public static void main(String[] args) {
       SpringApplication.run(MqttApplication.class, args);
    }
}