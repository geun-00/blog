package com.spring.blog.dto;

import lombok.Data;

@Data
public class CreateAccessTokenRequest {

    private String refreshToken;
}
