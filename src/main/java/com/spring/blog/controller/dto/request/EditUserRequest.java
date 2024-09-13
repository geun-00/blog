package com.spring.blog.controller.dto.request;

import com.spring.blog.common.annotation.ConditionalValidation;
import org.springframework.web.multipart.MultipartFile;

@ConditionalValidation
public record EditUserRequest(
        String nickname,
        MultipartFile file
) {
}