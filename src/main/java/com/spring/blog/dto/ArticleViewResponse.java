package com.spring.blog.dto;

import com.spring.blog.domain.Article;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleViewResponse {

    private Long id;
    private String title;
    private String author;
    private String content;
    private long views;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getUser().getNickname();
        this.content = article.getContent();
        this.views = article.getViews();
        this.createdAt = article.getCreatedAt();
        this.comments = article.getComments()
                .stream()
                .map(CommentResponse::new)
                .toList();
    }
}