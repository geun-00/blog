package com.spring.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {

    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),

    NONE("none");

    private final String socialName;
}