package com.spring.blog.service.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.blog.domain.User;

public record UserInfo(
        User user,
        Long countArticles,
        Long countComments) {

    @QueryProjection
    public UserInfo {
    }
}