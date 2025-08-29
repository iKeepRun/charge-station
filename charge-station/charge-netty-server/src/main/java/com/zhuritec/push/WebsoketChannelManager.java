package com.zhuritec.push;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理推送通道
 */
public class WebsoketChannelManager {
    // 创建channel容器
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    public static void addChannel(Channel channel) {
         channelGroup.add(channel);
//         channelMap.put(userId, channel);
    }
    public static void addChannelByUserId(String userId,Channel channel) {
//        channelGroup.add(channel);
        channelMap.put(userId, channel);
    }

    public static void removeChannel(Channel channel) {
        channelGroup.remove(channel);
    }

    public static void removeChannelByUserId(String userid) {
        channelMap.remove(userid);
    }

    public static Channel getChannelByUserId(String userid) {
        return channelMap.get(userid);
    }

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}
