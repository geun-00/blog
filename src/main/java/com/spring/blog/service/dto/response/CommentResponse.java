package com.spring.blog.service.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String username,
        String comment,
        String profileImageUrl,
        LocalDateTime createdAt,
        Boolean isAuthor
) {
    public Boolean isAuthor() {
        return isAuthor;
    }
}