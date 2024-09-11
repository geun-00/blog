package com.spring.blog.controller.dto.request;

import com.spring.blog.service.dto.request.CommentServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank
    private String comment;

    public CommentServiceRequest toServiceRequest() {
        return CommentServiceRequest.builder()
                .comment(comment)
                .build();
    }
}