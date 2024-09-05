package com.spring.blog.dto.request;

import lombok.Data;

@Data
public class NewPasswordRequest {

    private String email;
    private String newPassword;
}
