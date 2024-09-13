package com.spring.blog.dto.request;

import com.spring.blog.common.annotation.ValidArticleSearchRequest;
import com.spring.blog.common.enums.SearchType;

import java.time.LocalDate;

@ValidArticleSearchRequest
public record ArticleSearchRequest(
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