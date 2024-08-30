package com.spring.blog.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.blog.domain.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final long views;
    private final long countComment;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;

    @QueryProjection
    public ArticleListViewResponse(Article article, long countComment) {
        this.id = article.getId();
        this.views = article.getViews();
        this.title = article.getTitle();
        this.countComment = countComment;
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.author = article.getUser().getNickname();
    }
}
