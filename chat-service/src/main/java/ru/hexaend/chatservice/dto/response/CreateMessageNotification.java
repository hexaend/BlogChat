package ru.hexaend.chatservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMessageNotification {

    private String from;
    private String to;
    private String fromUsername;
    private Long chatId;
    private String content;
    private Long messageId;
    private Long timestamp;


}
