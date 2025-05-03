package ru.hexaend.post_service.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hexaend.post_service.services.PostService;

@RequestMapping("/moderator")
@RestController
@PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
@Tag(name = "Moderator Controller", description = "Moderator operations. Only for admins and moderators.")
public class ModeratorController {

    private final PostService postService;

    public ModeratorController(PostService postService) {
        this.postService = postService;
    }

    @DeleteMapping("/{postId}")
    @Operation(
            summary = "Delete post",
            description = "Delete post by moderator. Only for admins and moderators."
    )
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePostByAdmin(postId);
        return ResponseEntity.ok("Successfully deleted");
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    @Operation(
            summary = "Delete comment",
            description = "Delete comment by moderator. Only for admins and moderators."
    )
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        postService.deleteCommentByAdmin(postId, commentId);
        return ResponseEntity.ok("Successfully deleted");
    }
}
