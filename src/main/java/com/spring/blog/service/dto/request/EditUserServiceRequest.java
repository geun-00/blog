package com.spring.blog.service.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record EditUserServiceRequest(
        String nickname,
        MultipartFile file
) {
}