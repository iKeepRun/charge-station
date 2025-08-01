package com.zhuritec.message;

import lombok.Data;

@Data
public class ChargePayload {
    //开始标志 : 占1个字节
    private final static byte start_tag=0x76;

    //协议版本: 占1个字节
    private byte version;

    //充电桩ID : 占4个字节
    private short charge_id;

    //消息类型：占1个字节 1=操作指令消息，2=充电状态消息
    private byte type;

    //充电状态数据长度 : 占2个字节
    private short charge_stat_data_len;

    //充电状态数据 : 最多500个字节
    private byte[] charge_stat_data;

    //操作指令: 占1个字节
    private byte cmd;

    //校验码: 占1个字节
    private byte checksum;
    
    // 添加无参构造方法
    public ChargePayload() {
    }
    
    // 添加带参数的构造方法
    public ChargePayload(byte cmd) {
        this.cmd = cmd;
    }
}
