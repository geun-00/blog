package com.spring.blog.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OAuthAddUserServiceRequest {

    private String phoneNumber;
    private String nickname;
}