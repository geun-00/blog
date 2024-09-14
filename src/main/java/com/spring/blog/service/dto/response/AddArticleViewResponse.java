package com.spring.blog.service.dto.response;

public record AddArticleViewResponse(
        Long id,
        String title,
        String content
) {

    public static AddArticleViewResponse of() {
        return new AddArticleViewResponse(null, null, null);
    }
}