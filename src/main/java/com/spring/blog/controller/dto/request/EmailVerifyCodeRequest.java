package com.spring.blog.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EmailVerifyCodeRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Length(min = 6)
        String verificationCode
) {
}