package ru.hexaend.chatservice.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hexaend.chatservice.dto.request.ChatMessage;
import ru.hexaend.chatservice.dto.request.DeleteMessage;
import ru.hexaend.chatservice.dto.request.EditMessage;
import ru.hexaend.chatservice.dto.response.*;
import ru.hexaend.chatservice.feign.FeignUserService;
import ru.hexaend.chatservice.mappers.ChatMapper;
import ru.hexaend.chatservice.mappers.MessageMapper;
import ru.hexaend.chatservice.models.Message;
import ru.hexaend.chatservice.models.PrivateChat;
import ru.hexaend.chatservice.repositories.ChatRepository;
import ru.hexaend.chatservice.repositories.MessageRepository;
import ru.hexaend.chatservice.services.ChatService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final FeignUserService feignUserService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMapper chatMapper;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public ChatServiceImpl(ChatRepository chatRepository, FeignUserService feignUserService, SimpMessagingTemplate simpMessagingTemplate, ChatMapper chatMapper,
                           MessageRepository messageRepository, MessageMapper messageMapper) {
        this.chatRepository = chatRepository;
        this.feignUserService = feignUserService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMapper = chatMapper;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    @Transactional
    public void saveMessage(String from, ChatMessage chatMessage) {
        chatMessage.setTo(feignUserService.getUserId(chatMessage.getTo(), null)
                .getBody());

        String fromUsername = feignUserService.getUsername(from, null).getBody();

        PrivateChat privateChat = chatRepository.findByAuthorIdAndRecipientId(from, chatMessage.getTo())
                .orElseGet(
                        () -> {
                            return PrivateChat.builder()
                                    .authorId(from)
                                    .recipientId(chatMessage.getTo())
                                    .build();
                        }
                );

        Message message = Message.builder().content(chatMessage.getContent()).build();

        privateChat.addMessage(message);
        message = messageRepository.save(message);
        privateChat = chatRepository.save(privateChat);

        CreateMessageNotification createMessageNotification = new CreateMessageNotification(
                from,
                chatMessage.getTo(),
                fromUsername,
                privateChat.getId(),
                chatMessage.getContent(),
                message.getId(),
                message.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        simpMessagingTemplate.convertAndSendToUser(chatMessage.getTo(), "/queue/messages", createMessageNotification);
    }

    @Override
    @Transactional
    public void deleteMessage(String from, DeleteMessage chatMessage) {
        chatMessage.setUsername(feignUserService.getUserId(chatMessage.getUsername(), null)
                .getBody());
        String fromUsername = feignUserService.getUsername(from, null).getBody();

        PrivateChat privateChat = chatRepository.findByAuthorIdAndRecipientId(from, chatMessage.getUsername())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        privateChat.getMessages().removeIf(m -> Objects.equals(m.getId(), chatMessage.getMessageId()) && Objects.equals(m.getAuthorId(), from));

        chatRepository.save(privateChat);

        DeleteMessageNotification deleteMessageNotification = new DeleteMessageNotification(
                from,
                fromUsername,
                privateChat.getId(),
                chatMessage.getMessageId(),
                LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        simpMessagingTemplate.convertAndSendToUser(chatMessage.getUsername(), "/queue/messages/deleted", chatMessage.getMessageId());

    }

    @Override
    @Transactional
    public void editMessage(String from, EditMessage chatMessage) {
        chatMessage.setUsername(feignUserService.getUserId(chatMessage.getUsername(), null)
                .getBody());
        String fromUsername = feignUserService.getUsername(from, null).getBody();

        PrivateChat privateChat = chatRepository.findByAuthorIdAndRecipientId(from, chatMessage.getUsername())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        privateChat.setMessages(
                privateChat.getMessages().stream().filter((m) -> {
                    if (m.getId().equals(chatMessage.getMessageId()) && m.getAuthorId().equals(from)) {
                        m.setContent(chatMessage.getContent());
                        return true;
                    }
                    return false;
                }).toList()
        );

        chatRepository.save(privateChat);

        EditMessageNotification editMessageNotification = new EditMessageNotification(
                from,
                chatMessage.getUsername(),
                fromUsername,
                privateChat.getId(),
                chatMessage.getContent(),
                chatMessage.getMessageId(),
                LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        simpMessagingTemplate.convertAndSendToUser(chatMessage.getUsername(), "/queue/messages/edited", editMessageNotification);
    }

    @Override
    @Transactional
    public Page<PrivateChatDto> getAllChats(int page, int size, String sort, String order, Authentication authentication) {

        Sort sortObj = Sort.by(sort);
        if (Objects.equals(order, "desc")) {
            sortObj = sortObj.descending();
        } else {
            sortObj = sortObj.ascending();
        }

        Page<PrivateChat> privateChats = chatRepository.findAllByAuthorId(authentication.getName(), PageRequest.of(page, size, sortObj));

        return privateChats.map(chatMapper::toPrivateChatDto);
    }

    @Override
    public Page<MessageDto> getMessages(long chat, int page, int size, String sort, String order, Authentication authentication) {
        Sort sortObj = Sort.by(sort);
        if (Objects.equals(order, "desc")) {
            sortObj = sortObj.descending();
        } else {
            sortObj = sortObj.ascending();
        }

        PrivateChat privateChat = chatRepository.findFirstById(chat).orElseThrow(RuntimeException::new);

        Page<Message> messages = messageRepository.findAllByPrivateChat(privateChat, PageRequest.of(page, size, sortObj));

        return messages.map(messageMapper::toMessageDto);
    }
}
