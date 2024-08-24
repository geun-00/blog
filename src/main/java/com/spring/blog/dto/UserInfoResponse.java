package com.spring.blog.dto;

import com.spring.blog.common.converters.enums.SocialType;
import com.spring.blog.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoResponse {

    private String email;
    private String username;
    private LocalDateTime createdAt;
    private SocialType registrationId;
    private Long countArticles;

    public UserInfoResponse(User user, Long countArticles) {
        this.email = user.getEmail();
        this.username = user.getNickname();
        this.createdAt = user.getCreatedAt();
        this.registrationId = user.getRegistrationId();
        this.countArticles = countArticles;
    }
}