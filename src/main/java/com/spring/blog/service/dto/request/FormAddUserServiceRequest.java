package com.spring.blog.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FormAddUserServiceRequest {

    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
}