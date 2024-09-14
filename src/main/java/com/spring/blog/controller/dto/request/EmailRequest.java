package com.spring.blog.controller.dto.request;

import jakarta.validation.constraints.Email;

public record EmailRequest(
        @Email
        String email
) {
}