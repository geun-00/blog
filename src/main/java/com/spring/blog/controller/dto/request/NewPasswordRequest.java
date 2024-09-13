package com.spring.blog.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewPasswordRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$",
                message = "비밀번호 요구사항과 맞지 않습니다.")
        String newPassword
) {
}