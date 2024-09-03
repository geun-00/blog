package com.spring.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResponse {

    private boolean liked;
    private int likesCount;
}