package com.spring.blog.mapper;

import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.dto.request.ArticleSearchRequest;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleServiceRequest toServiceRequest(ArticleRequest request);
    ArticleSearchServiceRequest toServiceRequest(ArticleSearchRequest request);
    Article toEntity(ArticleServiceRequest request, User user);
}