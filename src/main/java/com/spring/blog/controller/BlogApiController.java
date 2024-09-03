package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.domain.Article;
import com.spring.blog.dto.request.AddArticleRequest;
import com.spring.blog.dto.response.ArticleListViewResponse;
import com.spring.blog.dto.response.ArticleResponse;
import com.spring.blog.dto.request.ArticleSearchRequest;
import com.spring.blog.dto.response.PageResponse;
import com.spring.blog.dto.request.UpdateArticleRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request,
                                                      @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        Article savedArticle = blogService.save(request, principalUser.providerUser().getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ArticleResponse(savedArticle));
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

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") long id,
                                                         @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok().body(new ArticleResponse(updatedArticle));
    }

    @PostMapping("/articles/like/{articleId}")
    public ResponseEntity<Void> addLike(@PathVariable("articleId") Long articleId,
                                        @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        blogService.addLike(articleId, principalUser.providerUser().getEmail());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles/{articleId}/liked")
    public ResponseEntity<Boolean> isLiked(@PathVariable("articleId") Long articleId,
                                           @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        boolean liked = blogService.isLiked(articleId, principalUser.providerUser().getEmail());

        return ResponseEntity.ok(liked);
    }
}