package com.zhuritec.mqtt.factory.conf;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "mqtt")
public class Mqttconfig {
    private String host;
    private String clientId;
    private String username;
    private String password;
    private MqttConnectOptions options;
    private String topic;

}
