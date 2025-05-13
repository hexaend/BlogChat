package ru.hexaend.chatservice.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hexaend.chatservice.dto.response.MessageDto;
import ru.hexaend.chatservice.dto.response.PrivateChatDto;
import ru.hexaend.chatservice.services.ChatService;

@RestController
@RequestMapping("/chats")
public class MessagesController {

    private final ChatService chatService;

    public MessagesController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping()
    public ResponseEntity<Page<PrivateChatDto>> getAllChats(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sort,
                                                            @RequestParam(defaultValue = "asc") String order,
                                                            Authentication authentication) {
        return ResponseEntity.ok(chatService.getAllChats(page, size, sort, order, authentication));
    }

    @GetMapping("/messages/{chat}")
    public ResponseEntity<Page<MessageDto>> getMessages(
            @PathVariable long chat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            Authentication authentication) {

        return ResponseEntity.ok(chatService.getMessages(chat, page, size, sort, order, authentication));
    }
}
