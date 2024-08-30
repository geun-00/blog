package com.spring.blog.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddUserRequest {

    private String email;
    private String password;
    private String nickname;
}