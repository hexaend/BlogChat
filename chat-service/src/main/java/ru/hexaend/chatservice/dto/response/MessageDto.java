package ru.hexaend.chatservice.dto.response;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class MessageDto {

    Long id;
    String authorId;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
