package com.spring.blog.controller.api;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleResponse;
import com.spring.blog.service.dto.response.LikeResponse;
import com.spring.blog.service.dto.response.PageResponse;
import com.spring.blog.mapper.ArticleMapper;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogService blogService;
    private final ArticleMapper articleMapper;

    @PostMapping("/articles")
    public ApiResponse<ArticleResponse> addArticle(@Validated @RequestBody ArticleRequest request,
                                                   @CurrentUser PrincipalUser principalUser,
                                                   @CookieValue("JSESSIONID") String sessionId) {

        ArticleResponse response = blogService.save(
                articleMapper.toServiceRequest(request),
                principalUser.providerUser().getEmail(),
                sessionId);

        return ApiResponse.of(
                HttpStatus.CREATED,
                response);
    }

    @PutMapping("/articles/{articleId}")
    public ApiResponse<ArticleResponse> updateArticle(@Validated @RequestBody ArticleRequest request,
                                                      @PathVariable("articleId") long articleId) {
        ArticleResponse response = blogService.update(
                articleMapper.toServiceRequest(request),
                articleId);

        return ApiResponse.of(
                HttpStatus.CREATED,
                response);
    }

    @DeleteMapping("/articles/{articleId}")
    public ApiResponse<Void> deleteArticle(@PathVariable("articleId") long articleId) {
        blogService.delete(articleId);

        return ApiResponse.ok(null);
    }

    @PostMapping("/articles/search")
    public ApiResponse<PageResponse<ArticleListViewResponse>> findAllArticlesByCond(
            @Validated @RequestBody ArticleSearchRequest request,
            @PageableDefault Pageable pageable) {

        PageResponse<ArticleListViewResponse> articles = blogService.findAllByCond(
                articleMapper.toServiceRequest(request),
                pageable);

        return ApiResponse.ok(articles);
    }

    @GetMapping("/articles/page")
    public ApiResponse<PageResponse<ArticleListViewResponse>> getPagingArticles(
            @PageableDefault(direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable) {

        PageResponse<ArticleListViewResponse> articles = blogService.findAll(pageable);

        return ApiResponse.ok(articles);
    }

    @PostMapping("/articles/like/{articleId}")
    public ApiResponse<Integer> addLike(@PathVariable("articleId") Long articleId,
                                        @CurrentUser PrincipalUser principalUser) {

        int likesCount = blogService.addLike(articleId, principalUser.providerUser().getEmail());

        return ApiResponse.ok(likesCount);
    }

    @DeleteMapping("/articles/like/{articleId}")
    public ApiResponse<Integer> deleteLike(@PathVariable("articleId") Long articleId,
                                           @CurrentUser PrincipalUser principalUser) {

        int likesCount = blogService.deleteLike(articleId, principalUser.providerUser().getEmail());

        return ApiResponse.ok(likesCount);
    }

    @GetMapping("/articles/{articleId}/liked")
    public ApiResponse<LikeResponse> isLiked(@PathVariable("articleId") Long articleId,
                                             @CurrentUser PrincipalUser principalUser) {

        LikeResponse response = blogService.isLiked(articleId, principalUser.providerUser().getEmail());

        return ApiResponse.ok(response);
    }
}