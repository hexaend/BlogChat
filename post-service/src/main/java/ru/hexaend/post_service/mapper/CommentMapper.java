package ru.hexaend.post_service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.hexaend.post_service.dto.request.CommentRequest;
import ru.hexaend.post_service.dto.response.CommentResponse;
import ru.hexaend.post_service.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment requestToEntity(CommentRequest postRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentRequest postRequest, @MappingTarget Comment post);

    static CommentResponse commentToResponse(Comment comment) {
        return CommentResponse.builder()
                .username(comment.getUsername())
                .id(comment.getId())
                .username(comment.getUsername())
                .content(comment.getContent())
                .build();
    }
}
//    static CommentResponse commentToResponse(Comment comment) {
//        return CommentResponse.builder().username(comment.getUsername()).id(comment.getId()).content(comment.getContent()).build();
//    }}
