package ru.hexaend.chatservice.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hexaend.chatservice.dto.response.PrivateChatDto;
import ru.hexaend.chatservice.repositories.ChatRepository;
import ru.hexaend.chatservice.services.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class MessagesController {

    private final ChatRepository chatRepository;
    private final ChatService chatService;

    public MessagesController(ChatRepository chatRepository, ChatService chatService) {
        this.chatRepository = chatRepository;
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

}
