package com.spring.blog.controller.stomp;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class StompController {

    @MessageMapping("/chat/{chatId}")
    @SendTo("/chat/{chatId}")
    public String message(ChatDto request, @DestinationVariable("chatId") Long chatId) {
        log.info("StompController.message");
        return "OK";
    }

    @Getter
    static class ChatDto {
        private String content;
    }
}
