package ru.hexaend.post_service.mapper;

import org.mapstruct.*;
import ru.hexaend.post_service.dto.request.PostRequest;
import ru.hexaend.post_service.dto.response.PostResponse;
import ru.hexaend.post_service.models.Post;

@Mapper(componentModel = "spring", uses = {CommentMapper.class}, imports = {java.util.ArrayList.class, CommentMapper.class},
        injectionStrategy = InjectionStrategy.FIELD)
public abstract class PostMapper {

    CommentMapper commentMapper;

    public abstract Post requestToEntity(PostRequest postRequest);

    @Mapping(target = "likesCount", expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)")
    @Mapping(target = "commentsCount", expression = "java(post.getComments() != null ? post.getComments().size() : 0)")
    @Mapping(target = "comments", expression = "java(post.getComments() != null ? post.getComments().stream().map(CommentMapper::commentToResponse).toList() : new ArrayList<>())")
    public abstract PostResponse postToResponse(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Post partialUpdate(PostRequest postRequest, @MappingTarget Post post);

}
