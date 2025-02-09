package com.chat.springbootwebsocketdemo.controller;

import com.chat.springbootwebsocketdemo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    // @SendTo用于处理结果并将处理结果发送到“/topic/public”主题。
    @SendTo("/topic/public")
    // @Payload 例如将 JSON 字符串转换为 Java 对象。
    // 在处理 WebSocket 消息时，通常会使用 @Payload 来接收客户端发送的数据。
    // 当消息体是一个复杂对象时，可以直接将消息体映射到一个 Java 类实例
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
    }

    /**
     *
     * @param chatMessage
     * @param headerAccessor
     * @return
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }


}
