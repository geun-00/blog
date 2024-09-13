package com.spring.blog.mapper;

import com.spring.blog.common.annotation.CommentResponseMapping;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = CommentResponseMapper.class
)
public interface ArticleMapper {

    ArticleServiceRequest toServiceRequest(ArticleRequest request);
    ArticleSearchServiceRequest toServiceRequest(ArticleSearchRequest request);

    @Mapping(target = "author", source = "article.user.nickname")
    @Mapping(target = "profileImageUrl", source = "article.user.profileImageUrl")
    @Mapping(target = "comments", source = "article", qualifiedBy = CommentResponseMapping.class)
    ArticleViewResponse toResponse(Article article);

    Article toEntity(ArticleServiceRequest request, User user);

}