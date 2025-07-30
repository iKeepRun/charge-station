package com.zhuritec.websocket.springanno.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MyWSHandler implements WebSocketHandler {
    
    // 存储所有连接的会话
    private static final ConcurrentHashMap<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(">>>>>基于Spring注解 连接建立成功 <<<<<");
        // 将会话存储到Map中，方便后续发送消息
        SESSIONS.put(session.getId(), session);
        log.info("当前连接数: {}", SESSIONS.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        //取出消息内容
        String payload = message.getPayload().toString();
        Object token = session.getAttributes().get("Token");
        log.info(">>>>>收到客户端消息:{}", payload);
        
        // 回复客户端消息
        sendMessageToClient(session, "服务器收到消息: " + payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info(">>>>>连接关闭: {}", session.getId());
        // 移除会话
        SESSIONS.remove(session.getId());
        log.info("当前连接数: {}", SESSIONS.size());
    }

    /**
     * 是否支持内容分片处理
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    /**
     * 发送消息给指定客户端
     * @param session WebSocket会话
     * @param message 消息内容
     */
    public void sendMessageToClient(WebSocketSession session, String message) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
                log.info("发送消息给客户端 {}: {}", session.getId(), message);
            }
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
    }
    
    /**
     * 发送消息给所有客户端
     * @param message 消息内容
     */
    public void sendMessageToAllClients(String message) {
        SESSIONS.values().forEach(session -> {
            sendMessageToClient(session, message);
            log.info("发送广播消息给所有客户端 {}",message);
        });
    }
    
    /**
     * 发送消息给指定客户端（通过会话ID）
     * @param sessionId 会话ID
     * @param message 消息内容
     */
    public void sendMessageToClientById(String sessionId, String message) {
        WebSocketSession session = SESSIONS.get(sessionId);
        if (session != null) {
            sendMessageToClient(session, message);
            log.info("发送消息 {} 给客户端 {}",message,sessionId);
        } else {
            log.warn("会话不存在: {}", sessionId);
        }
    }
    
    /**
     * 获取当前连接数
     * @return 连接数
     */
    public int getConnectionCount() {
        return SESSIONS.size();
    }
    
    /**
     * 获取所有会话ID
     * @return 会话ID集合
     */
    public Set<String> getAllSessionIds() {
        return SESSIONS.keySet();
    }
}
