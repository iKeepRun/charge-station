package com.zhuritec.handlers;

import com.zhuritec.message.ChargePayload;
import com.zhuritec.micro.MqttMicroClient;
import com.zhuritec.protobuf.ChargingCmdProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 自定义反序列化之后消息的处理类
 */
@Slf4j
@Component
public class MyProtoBufMsgHandler extends SimpleChannelInboundHandler<ChargingCmdProto.ChargingCmd> {
    @Resource
    private  MqttMicroClient mqttMicroClient;
//    public MyProtoBufMsgHandler() {
////        this.mqttMicroClient = null;
//    }
//    public MyProtoBufMsgHandler(MqttMicroClient mqttMicroClient) {
//        this.mqttMicroClient = mqttMicroClient;
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChargingCmdProto.ChargingCmd msg) throws Exception {
        String cmd = msg.getCmd();
        char c = cmd.charAt(0);
        byte cmdByte=(byte)c;
        log.info("接收到小程序端的控制指令:{}", cmd);
        log.info("接收到小程序端的控制指令:{}", cmdByte);
        ChargePayload chargePayload=new ChargePayload();
        chargePayload.setCmd(cmdByte);
        if (mqttMicroClient != null) {
            log.info("开始异步发送MQTT消息");
            CompletableFuture.runAsync(()->{
                try {
                    mqttMicroClient.send(chargePayload);
                    log.info("异步发送MQTT消息成功");
                } catch (Exception e) {
                    log.error("异步发送MQTT消息失败", e);
                }
            });
        }else {
            log.info("mqtt客户端没有初始化");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("WebSocket连接发生异常", cause);
        // 记录异常但不关闭连接，除非是严重错误
        // ctx.close();
    }
}
