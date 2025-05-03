package ru.hexaend.post_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Comment request DTO")
public class CommentRequest {

    @Schema(description = "Comment content", example = "This is a comment")
    @NotNull(message = "Content cannot be null")
    private String content;
}
