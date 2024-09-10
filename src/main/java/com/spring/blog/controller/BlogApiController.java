package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.domain.Article;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.dto.request.ArticleSearchRequest;
import com.spring.blog.dto.response.ArticleListViewResponse;
import com.spring.blog.dto.response.ArticleResponse;
import com.spring.blog.dto.response.LikeResponse;
import com.spring.blog.dto.response.PageResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/articles")
    public ApiResponse<ArticleResponse> addArticle(@Validated @RequestBody ArticleRequest request,
                                                   @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        Article savedArticle = blogService.save(
                request.toServiceRequest(),
                principalUser.providerUser().getEmail());

        return ApiResponse.of(
                HttpStatus.CREATED,
                new ArticleResponse(savedArticle));
    }

    @PutMapping("/articles/{articleId}")
    public ApiResponse<ArticleResponse> updateArticle(@Validated @RequestBody ArticleRequest request,
                                                      @PathVariable("articleId") long articleId) {
        Article updatedArticle = blogService.update(request.toServiceRequest(), articleId);

        return ApiResponse.of(
                HttpStatus.CREATED,
                new ArticleResponse(updatedArticle));
    }

    @PostMapping("/articles/search")
    public ResponseEntity<PageResponse<ArticleListViewResponse>> findAllArticlesByCond(
            @RequestBody ArticleSearchRequest request,
            @PageableDefault Pageable pageable) {

        PageResponse<ArticleListViewResponse> articles = blogService.findAllByCond(request, pageable);

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/articles/page")
    public ResponseEntity<PageResponse<ArticleListViewResponse>> getPagingArticles(
            @PageableDefault(direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable) {

        PageResponse<ArticleListViewResponse> articles = blogService.findAll(pageable);

        return ResponseEntity.ok().body(articles);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/articles/like/{articleId}")
    public ApiResponse<Integer> addLike(@PathVariable("articleId") Long articleId,
                                                        @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        int likesCount = blogService.addLike(articleId, principalUser.providerUser().getEmail());

        return ApiResponse.ok(likesCount);
    }

    @DeleteMapping("/articles/like/{articleId}")
    public ApiResponse<Integer> deleteLike(@PathVariable("articleId") Long articleId,
                                           @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        int likesCount = blogService.deleteLike(articleId, principalUser.providerUser().getEmail());

        return ApiResponse.ok(likesCount);
    }

    @GetMapping("/articles/{articleId}/liked")
    public ResponseEntity<LikeResponse> isLiked(@PathVariable("articleId") Long articleId,
                                                @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        LikeResponse response = blogService.isLiked(articleId, principalUser.providerUser().getEmail());

        return ResponseEntity.ok(response);
    }
}