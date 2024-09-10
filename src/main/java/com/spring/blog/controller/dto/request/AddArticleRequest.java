package com.spring.blog.controller.dto.request;

import com.spring.blog.common.annotation.NotBlankContent;
import com.spring.blog.service.dto.request.AddArticleServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleRequest {

    @NotBlank
    private String title;

    @NotBlankContent
    private String content;

    public AddArticleServiceRequest toServiceRequest() {
        return AddArticleServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }
}
