package com.spring.blog.service.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.blog.domain.Article;

public record ArticleInfo(
        Article article,
        Long commentCount
) {

    @QueryProjection
    public ArticleInfo {
    }
}