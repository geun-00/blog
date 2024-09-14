package com.spring.blog.service.dto.response;

import java.time.LocalDateTime;

public record ArticleListViewResponse(
        Long id,
        long views,
        long commentCount,
        String title,
        String author,
        LocalDateTime createdAt
) {
}
