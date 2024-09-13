package com.spring.blog.service.dto.request;

public record OAuthAddUserServiceRequest(
        String phoneNumber,
        String nickname
) {
}