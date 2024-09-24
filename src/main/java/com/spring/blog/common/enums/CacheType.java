package com.spring.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {

    USER("user", 5 * 60, 10000);

    private final String cacheName;
    private final int expireAfterWrite;
    private final int maximumSize;
}