package ru.hexaend.chatservice.mappers;

import org.mapstruct.Mapper;
import ru.hexaend.chatservice.dto.response.MessageDto;
import ru.hexaend.chatservice.models.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto toMessageDto(Message message);
}
