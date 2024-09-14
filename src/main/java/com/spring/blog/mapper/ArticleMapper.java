package com.spring.blog.mapper;

import com.spring.blog.common.annotation.CommentResponseMapping;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.ArticleImages;
import com.spring.blog.domain.ArticleLikes;
import com.spring.blog.domain.User;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleInfo;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleResponse;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.service.dto.response.LikeResponse;
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
    ArticleViewResponse toArticleViewResponse(Article article);

    AddArticleViewResponse toAddArticleViewResponse(Article article);
    ArticleResponse toArticleResponse(Article article);
    LikeResponse toLikeResponse(Boolean liked, Integer likesCount);

    @Mapping(target = "author", source = "article.user.nickname")
    @Mapping(target = "id", source = "article.id")
    @Mapping(target = "views", source = "article.views")
    @Mapping(target = "createdAt", source = "article.createdAt")
    @Mapping(target = "title", source = "article.title")
    ArticleListViewResponse toArticleListViewResponse(ArticleInfo articleInfo);

    Article toEntity(ArticleServiceRequest request, User user);
    ArticleImages toEntity(Article article, String imageUrl);

    @Mapping(target = "article", source = "article")
    @Mapping(target = "user", source = "user")
    ArticleLikes toEntity(User user, Article article);
}