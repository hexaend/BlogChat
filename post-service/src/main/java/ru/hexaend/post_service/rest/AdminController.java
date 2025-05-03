package ru.hexaend.post_service.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.hexaend.post_service.dto.request.PostRequest;
import ru.hexaend.post_service.dto.response.PostResponse;
import ru.hexaend.post_service.services.PostService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Admin Controller", description = "Admin operations. Only for admins.")
public class AdminController {

    private final PostService postService;

    public AdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all posts",
            description = "Get all posts with pagination and sorting. Only for admins. Includes deleted posts."
    )
    public ResponseEntity<?> getAllComments(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sort,
                                         @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(postService.getAllPostsAdmin(page, size, sort, order));
    }

    @PutMapping("/{postId}")
    @Operation(
            summary = "Update post",
            description = "Update post by admin. Only for admins."
    )
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequest postRequest) {
        PostResponse postResponse = postService.updatePostByAdmin(postId, postRequest);
        return ResponseEntity.ok(postResponse);
    }

}
