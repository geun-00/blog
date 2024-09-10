package com.spring.blog.controller.dto.request;

import com.spring.blog.common.annotation.NotBlankContent;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleRequest {

    @NotBlank
    private String title;

    @NotBlankContent
    private String content;

    public ArticleServiceRequest toServiceRequest() {
        return ArticleServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }
}