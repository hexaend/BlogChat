package ru.hexaend.post_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Comment response DTO")
public class CommentResponse {

    @Schema(description = "Comment ID", example = "1")
    Long id;
    @Schema(description = "Comment content", example = "This is a comment")
    String content;
    @Schema(description = "Username of the user who made the comment", example = "user123")
    String username;
}
