package com.zhuritec.mqtt.pacho.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class MqttConfig {
    @Value("${mqtt.host}")
    private String host;
    @Value("${mqtt.clientId}")
    private String clientId;
    @Value("${mqtt.options.username}")
    private String username;
    @Value("${mqtt.options.password}")
    private String password;

}
