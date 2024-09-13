package com.spring.blog.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleViewResponse(
        Long id,
        int likes,
        long views,
        String title,
        String author,
        String content,
        String profileImageUrl,
        LocalDateTime createdAt,
        List<CommentResponse> comments
) {
}