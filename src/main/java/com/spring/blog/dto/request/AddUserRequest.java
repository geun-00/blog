package com.spring.blog.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AddUserRequest {

    private String email;
    private String password;
    private String nickname;
    private MultipartFile imageFile;
}