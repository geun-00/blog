package com.spring.blog.service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentServiceRequest {

    private String comment;
}