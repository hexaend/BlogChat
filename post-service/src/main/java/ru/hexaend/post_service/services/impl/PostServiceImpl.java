package ru.hexaend.post_service.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hexaend.post_service.dto.request.PostRequest;
import ru.hexaend.post_service.dto.response.PostResponse;
import ru.hexaend.post_service.exceptions.NotFoundLikeException;
import ru.hexaend.post_service.exceptions.NotFoundPostException;
import ru.hexaend.post_service.exceptions.PostException;
import ru.hexaend.post_service.mapper.PostMapper;
import ru.hexaend.post_service.models.Like;
import ru.hexaend.post_service.models.Post;
import ru.hexaend.post_service.repositories.LikeRepository;
import ru.hexaend.post_service.repositories.PostRepository;
import ru.hexaend.post_service.services.PostService;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostServiceImpl(PostMapper postMapper, PostRepository postRepository, LikeRepository likeRepository) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    @Transactional
//    @Retry(name = "postServiceRetry", fallbackMethod = "createPostFallback")
    public PostResponse createPost(PostRequest postRequest, Authentication authentication) {
        Post post = postMapper.requestToEntity(postRequest);
        post.setUsername(((Jwt) authentication.getPrincipal()).getClaim("preferred_username"));
        post = postRepository.save(post);
        return postMapper.postToResponse(post);
    }

    @Override
    @Transactional
//    @CircuitBreaker(name = "postService", fallbackMethod = "getPostFallback")
//    @Retry(name = "postServiceRetry", fallbackMethod = "getPostFallback")
    public PostResponse getPost(Long id, Authentication authentication) {
        Post post = postRepository.findByIdAndDeletedIsFalse(id).orElseThrow(NotFoundPostException::new);
        if (!post.getAuthor().equals(authentication.getName())) {
            post.addView();
            post = postRepository.save(post);
        }
        return postMapper.postToResponse(post);
    }


    @Override
    @Transactional
//    @Retry(name = "postServiceRetry", fallbackMethod = "updatePostFallback")
    public PostResponse updatePost(Long id, PostRequest postRequest, Authentication authentication) {
        Post post = postRepository.findByIdAndDeletedIsFalseAndAuthor(id, authentication.getName()).orElseThrow(NotFoundPostException::new);

        postMapper.partialUpdate(postRequest, post);
        post =postRepository.save(post);

        return postMapper.postToResponse(post);
    }

    @Override
    @Transactional
//    @Retry(name = "postServiceRetry", fallbackMethod = "deletePostFallback")
    public void deletePost(Long id, Authentication authentication) {
        Post post = postRepository.findByIdAndDeletedIsFalseAndAuthor(id, authentication.getName()).orElseThrow(NotFoundPostException::new);

        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    @Transactional
//    @Retry(name = "postServiceRetry", fallbackMethod = "likePostFallback")
    public PostResponse likePost(Long postId, Authentication authentication) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        Optional<Like> like = likeRepository.findByPostAndAuthor(post, authentication.getName());

        if (like.isPresent()) {
            post.removeLike(like.get());
            likeRepository.delete(like.get());
            post =  postRepository.save(post);
            return postMapper.postToResponse(post);
        } else {
            like = Optional.of(Like.builder().username(((Jwt) authentication.getPrincipal()).getClaim("preferred_username")).post(post).build());
            post.addLike(like.get());
            post =  postRepository.save(post);
            return postMapper.postToResponse(post);
        }
    }

    @Override
//    @CircuitBreaker(name = "postService", fallbackMethod = "getAllPostFallback")
    public Page<PostResponse> getAllPosts(int page, int size, String sort, String order, Authentication authentication) {
        Sort sortObj = Sort.by(sort);
        if (Objects.equals(order, "desc")) {
            sortObj = sortObj.descending();
        } else {
            sortObj = sortObj.ascending();
        }

        Page<Post> posts = postRepository.findAllByDeletedIsFalse(PageRequest.of(page, size, sortObj));
        posts.stream().filter(p -> !Objects.equals(p.getAuthor(), authentication.getName())).forEach(Post::addView);
        postRepository.saveAll(posts);
        posts = postRepository.findAllByDeletedIsFalse(PageRequest.of(page, size, sortObj));
        return posts.map(postMapper::postToResponse);
    }

    public Page<PostResponse> getAllPostFallback(int page, int size, String sort, String order, Authentication authentication, Throwable t) {
        log.error("Fallback for getAllPosts, reason: {}", t.toString());
        throw new PostException("Посты не найдены. Попробуйте позже");
    }

    @Override
//    @CircuitBreaker(name = "postService", fallbackMethod = "getAllPostFallback")
    public Page<PostResponse> getAllPostsAdmin(int page, int size, String sort, String order) {

        Sort sortObj = Sort.by(sort);
        if (Objects.equals(order, "desc")) {
            sortObj = sortObj.descending();
        } else {
            sortObj = sortObj.ascending();
        }

        Page<Post> posts = postRepository.findAll(PageRequest.of(page, size, sortObj));
        posts.stream().forEach(Post::addView);
        postRepository.saveAll(posts);
        posts = postRepository.findAll(PageRequest.of(page, size, sortObj));
        return posts.map(postMapper::postToResponse);
    }

    @Override
    @Transactional
    public PostResponse updatePostByAdmin(Long postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        postMapper.partialUpdate(postRequest, post);
        post =  postRepository.save(post);
        return postMapper.postToResponse(post);
    }

    @Override
    @Transactional
//    @Retry(name = "postServiceRetry", fallbackMethod = "deletePostAdminFallback")
    public void deletePostByAdmin(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    @Transactional
//    @Retry(name = "postServiceRetry", fallbackMethod = "deleteCommentFallback")
    public void deleteCommentByAdmin(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        post.getComments().removeIf(comment -> comment.getId().equals(commentId));
        postRepository.save(post);
    }

    //    @CircuitBreaker(name = "postService", fallbackMethod = "getPostFallback")
    @Override
    public Page<PostResponse> getMyAllPosts(int page, int size, String sort, String order, Authentication authentication) {
        Sort sortObj = Sort.by(sort);
        if (Objects.equals(order, "desc")) {
            sortObj = sortObj.descending();
        } else {
            sortObj = sortObj.ascending();
        }

        Page<Post> posts = postRepository.findAllByDeletedIsFalseAndAuthor(authentication.getName(), PageRequest.of(page, size, sortObj));
        postRepository.saveAll(posts);
        posts = postRepository.findAllByDeletedIsFalseAndAuthor(authentication.getName(), PageRequest.of(page, size, sortObj));
        return posts.map(postMapper::postToResponse);
    }


    public PostResponse createPostFallback(PostRequest postRequest, Authentication authentication, Throwable t) {
        log.error("Fallback for createPost, reason: {}", t.toString());
        throw new PostException("Пост не создан. Попробуйте позже");
    }

    public PostResponse getPostFallback(Long id, Authentication authentication, Throwable t) {
        log.error("Fallback for getPost, reason: {}", t.toString());
        throw new PostException("Пост не найден. Попробуйте позже");
    }


    public PostResponse deletePostFallback(Long id, Authentication authentication, Throwable t) {
        log.error("Fallback for deletePost, reason: {}", t.toString());
        throw new PostException("Пост не удален. Попробуйте позже");
    }

    public PostResponse deletePostAdminFallback(Long id, Throwable t) {
        log.error("Fallback for deletePostAdmin, reason: {}", t.toString());
        throw new PostException("Пост не удален. Попробуйте позже");
    }

    public PostResponse deleteCommentFallback(Long postId, Long commentId, Throwable t) {
        log.error("Fallback for deleteCommentByAdmin, reason: {}", t.toString());
        throw new PostException("Комментарий не удален. Попробуйте позже");
    }

    public PostResponse likePostFallback(Long postId, Authentication authentication, Throwable t) {
        log.error("Fallback for likePost, reason: {}", t.toString());
        throw new PostException("Пост не лайкнут. Попробуйте позже");
    }

    public PostResponse updatePostFallback(Long id, PostRequest postRequest, Authentication authentication, Throwable t) {
        log.error("Fallback for updatePost, reason: {}", t.toString());
        throw new PostException("Пост не обновлен. Попробуйте позже");
    }
}
