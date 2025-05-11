package ru.hexaend.chatservice.dto.request;

import lombok.Data;

@Data
public class DeleteMessage {

    private String username;
    private Long messageId;

}
