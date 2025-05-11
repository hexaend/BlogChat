package ru.hexaend.chatservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteMessageNotification {
    private String from;
    private String fromUsername;
    private Long chatId;
    private Long messageId;
    private Long timestamp;

}
