package ru.hexaend.chatservice.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ChatMessage {

    private String to;
    private String content;


}
