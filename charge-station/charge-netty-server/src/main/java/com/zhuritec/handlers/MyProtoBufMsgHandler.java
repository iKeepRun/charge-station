package com.zhuritec.handlers;

import com.zhuritec.message.ChargePayload;
import com.zhuritec.micro.MqttMicroClient;
import com.zhuritec.protobuf.ChargingCmdProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义反序列化之后消息的处理类
 */
@Slf4j
@Component
public class MyProtoBufMsgHandler extends SimpleChannelInboundHandler<ChargingCmdProto.ChargingCmd> {

    private final MqttMicroClient mqttMicroClient;

    public MyProtoBufMsgHandler(MqttMicroClient mqttMicroClient) {
        this.mqttMicroClient = mqttMicroClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChargingCmdProto.ChargingCmd msg) throws Exception {
        String cmd = msg.getCmd();
        char c = cmd.charAt(0);
        byte cmdByte=(byte)c;
        log.info("接收到小程序端的控制指令:{}", cmd);
        ChargePayload chargePayload=new ChargePayload();
        chargePayload.setCmd(cmdByte);
        mqttMicroClient.send(chargePayload);
    }
}
