package com.spring.blog.service.dto.request;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleServiceRequest {

    private String title;
    private String content;

    public Article toEntity(User user) {
        return Article.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}