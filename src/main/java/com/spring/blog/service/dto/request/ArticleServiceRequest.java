package com.spring.blog.service.dto.request;

public record ArticleServiceRequest(
        String title,
        String content) {
}