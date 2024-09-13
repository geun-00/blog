package com.spring.blog.controller.dto.request;

import com.spring.blog.common.annotation.NotBlankContent;
import jakarta.validation.constraints.NotBlank;

public record ArticleRequest(

        @NotBlank
        String title,

        @NotBlankContent
        String content) { }