package com.spring.blog.dto;

import com.spring.blog.domain.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final long views;
    private final int countComment;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.views = article.getViews();
        this.title = article.getTitle();
        this.countComment = article.getComments().size();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.author = article.getUser().getNickname();
    }
}
