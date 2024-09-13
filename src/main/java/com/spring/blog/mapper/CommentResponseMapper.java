package com.spring.blog.mapper;

import com.spring.blog.common.annotation.CommentResponseMapping;
import com.spring.blog.domain.Article;
import com.spring.blog.service.dto.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentResponseMapper {

    private final CommentMapper commentMapper;

    @CommentResponseMapping
    public List<CommentResponse> commentResponses(Article article) {
        return article.getComments()
                .stream()
                .map(comment -> {
                    boolean isAuthor = StringUtils.equals(comment.getUser().getNickname(), article.getUser().getNickname());
                    return commentMapper.toResponse(comment, isAuthor);
                })
                .toList();
    }
}