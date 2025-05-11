package ru.hexaend.chatservice.services;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.hexaend.chatservice.dto.request.ChatMessage;
import ru.hexaend.chatservice.dto.request.DeleteMessage;
import ru.hexaend.chatservice.dto.request.EditMessage;
import ru.hexaend.chatservice.dto.response.PrivateChatDto;

import java.util.List;

public interface ChatService {
    void saveMessage(String from, ChatMessage chatMessage) ;
    void deleteMessage(String from, DeleteMessage chatMessage);
    void editMessage(String from, EditMessage messageDto);
    Page<PrivateChatDto> getAllChats(int page, int size, String sort, String order, Authentication authentication);
}
