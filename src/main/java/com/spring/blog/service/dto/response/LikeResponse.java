package com.spring.blog.service.dto.response;

public record LikeResponse(
        boolean liked,
        int likesCount
) {
}