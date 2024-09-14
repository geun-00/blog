package com.spring.blog.service.dto.response;

import com.spring.blog.common.enums.SocialType;

import java.time.LocalDateTime;

public record UserInfoResponse(
        String email,
        String username,
        String profileImageUrl,
        LocalDateTime createdAt,
        SocialType registrationId,
        Long countArticles,
        Long countComments
) {
}