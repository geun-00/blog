package com.spring.blog.service.dto.request;

public record FormAddUserServiceRequest(
        String email,
        String password,
        String nickname,
        String phoneNumber) {
}