package com.spring.blog.service.dto.request;

public record NewPasswordServiceRequest(
        String email,
        String newPassword
) {
}