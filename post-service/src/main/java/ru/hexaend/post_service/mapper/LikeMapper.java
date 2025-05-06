package ru.hexaend.post_service.mapper;

import org.mapstruct.Mapper;
import ru.hexaend.post_service.dto.response.LikeResponse;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    static LikeResponse likeToResponse(ru.hexaend.post_service.models.Like like) {
        return LikeResponse.builder()
                .author(like.getAuthor())
                .username(like.getUsername())
                .id(like.getId())
                .build();
    }
}
