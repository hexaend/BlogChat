package ru.hexaend.post_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data@Builder
public class LikeResponse {

    private String author;
    private String username;
    private Long id;


}
