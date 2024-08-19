package com.spring.blog.common.converters.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {

    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String socialName;
}