package com.spring.blog.mapper;

import com.spring.blog.controller.dto.request.CommentRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.service.dto.request.CommentServiceRequest;
import com.spring.blog.service.dto.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentServiceRequest toServiceRequest(CommentRequest request);

    @Mapping(target = "content", source = "request.content")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "article", source = "article")
    Comment toEntity(CommentServiceRequest request, User user, Article article);

    @Mapping(target = "author", source = "comment.user.nickname")
    @Mapping(target = "comment", source = "comment.content")
    @Mapping(target = "profileImageUrl", source = "comment.user.profileImageUrl")
    CommentResponse toResponse(Comment comment, boolean isAuthor);
}