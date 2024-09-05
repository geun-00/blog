package com.spring.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailVerifyCodeRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 6)
    private String verificationCode;
}