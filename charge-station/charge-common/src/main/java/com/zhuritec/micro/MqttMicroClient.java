package com.zhuritec.micro;

import com.zhuritec.message.ChargePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "charge-mqtt-client", url = "http://localhost:8686")
public interface MqttMicroClient {
    @RequestMapping(path = "/mqtt/send",method = RequestMethod.POST)
    public String  send(@RequestBody ChargePayload msg);
}
