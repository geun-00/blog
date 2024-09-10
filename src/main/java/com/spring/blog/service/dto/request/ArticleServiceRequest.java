package com.spring.blog.service.dto.request;

import com.spring.blog.domain.Article;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleServiceRequest {

    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}