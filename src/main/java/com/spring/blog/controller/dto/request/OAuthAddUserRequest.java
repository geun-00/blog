package com.spring.blog.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record OAuthAddUserRequest(
        @NotBlank
        @Pattern(regexp = "^010\\d{8}$",
                message = "올바른 형식의 전화번호여야 합니다.")
        String phoneNumber,

        @NotBlank
        @Length(min = 2, max = 20)
        String nickname
) {
}