package com.spring.blog.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.blog.common.enums.SocialType;
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
    private Long countComments;

    @QueryProjection
    public UserInfoResponse(User user, Long countArticles, Long countComments) {
        this.email = user.getEmail();
        this.username = user.getNickname();
        this.createdAt = user.getCreatedAt();
        this.registrationId = user.getRegistrationId();
        this.countArticles = countArticles;
        this.countComments = countComments;
    }
}