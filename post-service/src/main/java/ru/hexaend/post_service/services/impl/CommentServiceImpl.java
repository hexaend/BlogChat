package ru.hexaend.post_service.services.impl;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hexaend.post_service.dto.request.CommentRequest;
import ru.hexaend.post_service.dto.response.PostResponse;
import ru.hexaend.post_service.exceptions.NotFoundCommentException;
import ru.hexaend.post_service.exceptions.NotFoundPostException;
import ru.hexaend.post_service.exceptions.PostException;
import ru.hexaend.post_service.mapper.CommentMapper;
import ru.hexaend.post_service.mapper.PostMapper;
import ru.hexaend.post_service.models.Comment;
import ru.hexaend.post_service.models.Post;
import ru.hexaend.post_service.repositories.CommentRepository;
import ru.hexaend.post_service.repositories.PostRepository;
import ru.hexaend.post_service.services.CommentService;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, PostMapper postMapper, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    @Transactional
    @Retry(name = "postServiceRetry", fallbackMethod = "createCommentFallback")
    public PostResponse createComment(Long id, CommentRequest commentRequest, Authentication authentication) {
        Comment comment = commentMapper.requestToEntity(commentRequest);
        comment.setUsername(((Jwt) authentication.getPrincipal()).getClaim("preferred_username"));

        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);

        post.addComment(comment);

        postRepository.save(post);
        return postMapper.postToResponse(post);
    }

    public PostResponse createCommentFallback(Long id, CommentRequest commentRequest, Authentication authentication, Throwable t) {
        log.error("Error creating comment for post with id {}: {}", id, t.getMessage());
        throw new PostException("Комментарий не создан. Попробуйте позже");
    }

    @Override
    @Transactional
    @Retry(name = "postServiceRetry", fallbackMethod = "updateCommentFallback")
    public PostResponse updateComment(Long postId, Long commentId, CommentRequest commentRequest, Authentication authentication) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        Comment comment = commentRepository.findByIdAndAuthor(commentId, authentication.getName()).orElseThrow(NotFoundCommentException::new);
        comment = commentMapper.partialUpdate(commentRequest, comment);
        commentRepository.save(comment);

        return postMapper.postToResponse(post);
    }

    public PostResponse updateCommentFallback(Long postId, Long commentId, CommentRequest commentRequest, Authentication authentication, Throwable e) {
        log.error("Error updating comment with id {} for post with id {}: {}", commentId, postId, e.getMessage());
        throw new PostException("Комментарий не обновлён. Попробуйте позже");
    }

    @Override
    @Transactional
    @Retry(name = "postServiceRetry", fallbackMethod = "deleteCommentFallback")
    public void deleteComment(Long postId, Long commentId, Authentication authentication) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        Comment comment = commentRepository.findByIdAndAuthor(commentId, authentication.getName()).orElseThrow(NotFoundCommentException::new);

        post.removeComment(comment);
        commentRepository.delete(comment);
        postRepository.save(post);
    }

    public void deleteCommentFallback(Long postId, Long commentId, Authentication authentication, Throwable e) {
        log.error("Error deleting comment with id {} for post with id {}: {}", commentId, postId, e.getMessage());
        throw new PostException("Комментарий не удалён. Попробуйте позже");
    }
}
