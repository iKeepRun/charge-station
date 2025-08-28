package com.zhuritec.handler;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 主题工厂的处理器类
 */
@Component
public class TopicHandlerFactory {
    private final List<TopicHandler> handlers;

    public TopicHandlerFactory(List<TopicHandler> handlers) {
        this.handlers = handlers;
    }


    public TopicHandler getHandler(String topic) {
        for (TopicHandler handler : handlers) {
            if (handler.supports(topic)) {
                return handler;
            }
        }
        return null;
    }
}
