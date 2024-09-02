package com.spring.blog.dto.response;

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
    private String profileImageUrl;
    private long views;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getUser().getNickname();
        this.profileImageUrl = article.getUser().getProfileImageUrl();
        this.content = article.getContent();
        this.views = article.getViews();
        this.createdAt = article.getCreatedAt();
        this.comments = article.getComments()
                .stream()
                .map(comment -> {
                    boolean isAuthor = comment.getUser().getNickname().equals(article.getUser().getNickname());
                    return new CommentResponse(comment, isAuthor);
                })
                .toList();
    }
}