package ru.hexaend.post_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Post response DTO")
public class PostResponse {

    @Schema(description = "Post ID", example = "1")
    private Long id;

    @Schema(description = "Post title", example = "Post title")
    private String title;

    @Schema(description = "Post content", example = "Post content")
    private String content;

    @Schema(description = "Post author", example = "author123")
    private String author;

    @Schema(description = "Username of the user who created the post", example = "user123")
    private String username;

    @Schema(description = "Post creation date and time")
    private LocalDateTime createdAt;

    @Schema(description = "Post last update date and time")
    private LocalDateTime updatedAt;

    @Schema(description = "Post likes count")
    private int likesCount;

    @Schema(description = "Post likes")
    private List<LikeResponse> likes;

    @Schema(description = "Post comments count")
    private int commentsCount;

    @Schema(description = "Post comments")
    private List<CommentResponse> comments;

    @Schema(description = "Post views count")
    private int viewsCount;

//    @Schema(description = "Liked by current user")
//    private boolean likedByCurrentUser;


}
