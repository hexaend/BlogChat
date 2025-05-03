package ru.hexaend.post_service.services;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.hexaend.post_service.dto.request.PostRequest;
import ru.hexaend.post_service.dto.response.PostResponse;

@Service
public interface PostService {

    PostResponse createPost(PostRequest postRequest, Authentication authentication);

    PostResponse getPost(Long id, Authentication authentication);

    PostResponse updatePost(Long id, PostRequest postRequest, Authentication authentication);

    void deletePost(Long id, Authentication authentication);

    PostResponse likePost(Long postId, Authentication authentication);

    PostResponse unlikePost(Long postId, Authentication authentication);

    Page<PostResponse> getAllPosts(int page, int size, String sort, String order);

    Page<PostResponse> getAllPostsAdmin(int page, int size, String sort, String order);

    PostResponse updatePostByAdmin(Long postId, PostRequest postRequest);

    void deletePostByAdmin(Long postId);

    void deleteCommentByAdmin(Long postId, Long commentId);
}
