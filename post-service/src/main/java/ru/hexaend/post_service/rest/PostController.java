package ru.hexaend.post_service.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hexaend.post_service.dto.request.CommentRequest;
import ru.hexaend.post_service.dto.request.PostRequest;
import ru.hexaend.post_service.dto.response.PostResponse;
import ru.hexaend.post_service.services.CommentService;
import ru.hexaend.post_service.services.PostService;

@Slf4j
@RequestMapping("/")
@RestController
@Tag(name = "Post Controller", description = "Post and Commentaries operations. For users.")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @Operation(
            summary = "Get all posts",
            description = "Get all posts with pagination and sorting. For users. Excludes deleted posts."
    )
    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(postService.getAllPosts(page, size, sort, order));
    }

    @PostMapping
    @Operation(
            summary = "Create post",
            description = "Create post. For users."
    )
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        PostResponse postResponse = postService.createPost(postRequest, authentication);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{postId}")
    @Operation(
            summary = "Get post",
            description = "Get post by id. For users."
    )
    public ResponseEntity<?> getPost(@PathVariable Long postId, Authentication authentication) {
        PostResponse postResponse = postService.getPost(postId, authentication);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}")
    @Operation(
            summary = "Update post",
            description = "Update post by id. For users."
    )
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        PostResponse postResponse = postService.updatePost(postId, postRequest, authentication);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    @Operation(
            summary = "Delete post",
            description = "Delete post by id. For users."
    )
    public ResponseEntity<?> deletePost(@PathVariable Long postId, Authentication authentication) {
        postService.deletePost(postId, authentication);
        return ResponseEntity.ok("Successfully deleted");
    }

    @PostMapping("/{postId}/comment")
    @Operation(
            summary = "Create comment",
            description = "Create comment. For users."
    )
    public ResponseEntity<?> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {
        PostResponse postResponse = commentService.createComment(postId, commentRequest, authentication);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}/comment/{commentId}")
    @Operation(
            summary = "Update comment",
            description = "Update comment by id. For users."
    )
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {
        PostResponse postResponse = commentService.updateComment(postId, commentId, commentRequest, authentication);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    @Operation(
            summary = "Delete comment",
            description = "Delete comment by id. For users."
    )
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        commentService.deleteComment(postId, commentId, authentication);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}/like")
    @Operation(
            summary = "Like post",
            description = "Like post by id. For users."
    )
    public ResponseEntity<?> likePost(@PathVariable Long postId, Authentication authentication) {
        PostResponse postResponse = postService.likePost(postId, authentication);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}/like")
    @Operation(
            summary = "Unlike post",
            description = "Unlike post by id. For users."
    )
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, Authentication authentication) {
        PostResponse postResponse = postService.unlikePost(postId, authentication);
        return ResponseEntity.ok(postResponse);
    }
}
