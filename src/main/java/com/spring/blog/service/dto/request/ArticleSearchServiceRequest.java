package com.spring.blog.service.dto.request;

import com.spring.blog.common.enums.SearchType;

import java.time.LocalDate;

public record ArticleSearchServiceRequest(
        SearchType searchType,
        String title,
        String content,
        String author,
        TitleContent titleContent,
        Period period
) {
    public record TitleContent(String title, String content) { }

    public record Period(LocalDate startDate, LocalDate endDate) { }
}