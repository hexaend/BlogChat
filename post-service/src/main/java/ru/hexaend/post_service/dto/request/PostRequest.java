package ru.hexaend.post_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ru.hexaend.post_service.models.Post;

import java.io.Serializable;

/**
 * DTO for {@link Post}
 */
@Value
@Schema(description = "Post request DTO")
public class PostRequest implements Serializable {
    @NotNull(message = "Title cannot be null")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    @Schema(description = "Post title", example = "Post title")
    String title;

    @NotNull(message = "Content cannot be null")
    @Schema(description = "Post content", example = "Post content")
    @Size(min = 10, message = "Content must be at least 10 characters")
    String content;
}