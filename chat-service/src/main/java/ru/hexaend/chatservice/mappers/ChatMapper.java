package ru.hexaend.chatservice.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hexaend.chatservice.dto.response.PrivateChatDto;
import ru.hexaend.chatservice.models.PrivateChat;


@Mapper(componentModel = "spring", uses = MessageMapper.class)
public interface ChatMapper {
    @Mapping(target = "messages", source = "messages")
    PrivateChatDto toPrivateChatDto(PrivateChat privateChat);
}
