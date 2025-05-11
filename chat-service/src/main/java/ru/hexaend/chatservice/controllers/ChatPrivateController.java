package ru.hexaend.chatservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import ru.hexaend.chatservice.dto.request.ChatMessage;
import ru.hexaend.chatservice.dto.request.DeleteMessage;
import ru.hexaend.chatservice.dto.request.EditMessage;
import ru.hexaend.chatservice.services.ChatService;

@Slf4j
@Controller
public class ChatPrivateController {

    private final ChatService chatService;

    public ChatPrivateController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage messageDto, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            chatService.saveMessage(authentication.getName(), messageDto);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @MessageMapping("/chat.delete")
    public void deleteMessage(DeleteMessage messageDto, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            chatService.deleteMessage(authentication.getName(), messageDto);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @MessageMapping("/chat.edit")
    public void editMessage(EditMessage messageDto, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            chatService.editMessage(authentication.getName(), messageDto);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}

