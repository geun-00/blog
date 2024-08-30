package com.spring.blog.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditUserRequest {

    private String email;
    private String nickname;
}