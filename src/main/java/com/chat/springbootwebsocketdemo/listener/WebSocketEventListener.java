package com.chat.springbootwebsocketdemo.listener;

import com.chat.springbootwebsocketdemo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * WebSocket事件监听器，用于处理WebSocket连接相关的事件
 */
@Component
public class WebSocketEventListener {
    // 日志记录器，用于记录WebSocket事件信息
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    // 用于发送消息的操作模板
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * 处理WebSocket连接事件
     *
     * @param event SessionConnectedEvent对象，表示一个WebSocket连接事件
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        logger.info("Received a new web socket connection");
    }

    /**
     * 处理WebSocket断开连接事件
     *
     * @param event SessionDisconnectEvent对象，表示一个WebSocket断开连接事件
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        // 包装消息头访问器，用于获取会话属性
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 从会话属性中获取用户名
        String username = (String)headerAccessor.getSessionAttributes().get("username");

        // 如果用户名不为空，则记录用户断开连接信息，并发送离开消息
        if(username != null){
            logger.info("User Disconnected:"+username);

            // 创建并配置离开消息
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            // 发送离开消息到/public主题
            messagingTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
