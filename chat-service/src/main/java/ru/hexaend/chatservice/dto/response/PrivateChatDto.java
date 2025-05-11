package ru.hexaend.chatservice.dto.response;


import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class PrivateChatDto {

    Long id;
    String authorId;
    String recipientId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<MessageDto> messages;

}
