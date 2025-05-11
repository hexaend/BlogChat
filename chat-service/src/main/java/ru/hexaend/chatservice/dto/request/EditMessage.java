package ru.hexaend.chatservice.dto.request;

import lombok.Data;

@Data
public class EditMessage {

    private String username;
    private Long messageId;
    private String content;

}
