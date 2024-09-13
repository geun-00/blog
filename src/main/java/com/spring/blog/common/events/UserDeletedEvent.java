package com.spring.blog.common.events;

import com.spring.blog.domain.ArticleImages;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDeletedEvent {

    private String profileImageUrl;
    private List<ArticleImages> articleImages;
}