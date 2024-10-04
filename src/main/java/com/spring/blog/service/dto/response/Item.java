package com.spring.blog.service.dto.response;

public record Item(
        String category,
        String fcstDate,
        String fcstTime,
        String fcstValue
) {
}