package com.zhuritec.conf;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
