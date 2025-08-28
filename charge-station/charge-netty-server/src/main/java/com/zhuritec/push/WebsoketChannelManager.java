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
         channelMap.put(channel.id().asLongText(), channel);
    }

    public static void removeChannel(Channel channel) {
        channelGroup.remove(channel);
        channelMap.remove(channel.id().asLongText());
    }

    public static Channel getChannelById(String id) {
        return channelMap.get(id);
    }

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}
