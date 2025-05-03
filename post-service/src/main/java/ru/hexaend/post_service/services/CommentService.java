package ru.hexaend.post_service.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.hexaend.post_service.dto.request.CommentRequest;
import ru.hexaend.post_service.dto.response.PostResponse;

@Service
public interface CommentService {

    PostResponse createComment(Long id, CommentRequest commentRequest, Authentication authentication);

    PostResponse updateComment(Long postId, Long commentId, CommentRequest commentRequest, Authentication authentication);

    void deleteComment(Long postId, Long commentId, Authentication authentication);
}
