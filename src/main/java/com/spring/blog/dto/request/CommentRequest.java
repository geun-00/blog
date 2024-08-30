package com.spring.blog.dto.request;

import lombok.Data;

@Data
public class CommentRequest {

    private Long articleId;
    private String comment;
}