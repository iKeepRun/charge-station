package com.zhuritec.api;

import com.zhuritec.conf.Mqttconfig;
import com.zhuritec.message.ChargePayload;
import com.zhuritec.service.MqttService;
import com.zhuritec.utils.TransformerUtils;
import jakarta.annotation.Resource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendApi {
    @Resource
    private MqttService mqttService;
   @Resource
    private Mqttconfig mqttconfig;


    /**
     * desc:
     *     mqtt报文格式： 固定报头+可变报头+有效负载
     *     固定报头： 2-5个字节
     *     有效负载：就是发送的具体消息（自定义类型 ChargePayload）
     * 发送消息
     * @param msg
     */
    @RequestMapping(path = "/mqtt/send",method = RequestMethod.POST)
    public String  send(@RequestBody ChargePayload msg) {
        String hex = TransformerUtils.objectToHex(msg);
        byte[] bytes = TransformerUtils.hexStringToByteArray(hex);
        Message<byte[]> payload = MessageBuilder.withPayload(bytes).build();
        //使用fastjson将消息进行转换
//        String msgStr = JSON.toJSONString(msg);
        mqttService.sendToMqtt(mqttconfig.getTopic(), payload);
        return "mqtt send success!";
    }
}
