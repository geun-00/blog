package com.spring.blog.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank
        String content
) {
}