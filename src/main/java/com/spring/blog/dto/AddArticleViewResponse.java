package com.spring.blog.dto;

import com.spring.blog.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddArticleViewResponse {

    private Long id;
    private String title;
    private String content;

    public AddArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}