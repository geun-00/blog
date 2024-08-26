package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.domain.Article;
import com.spring.blog.dto.AddArticleRequest;
import com.spring.blog.dto.ArticleListViewResponse;
import com.spring.blog.dto.ArticleResponse;
import com.spring.blog.dto.ArticleSearchRequest;
import com.spring.blog.dto.UpdateArticleRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogService blogService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/api/articles")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request,
                                                      @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        Article savedArticle = blogService.save(request, principalUser.providerUser().getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ArticleResponse(savedArticle));
    }

    @PostMapping("/articles/search")
    public ResponseEntity<List<ArticleListViewResponse>> findAllArticles(@RequestBody ArticleSearchRequest request) {

        List<ArticleListViewResponse> articles = blogService.findAllByCond(request)
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();

        return ResponseEntity.ok().body(articles);
    }


    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") long id) {
        Article article = blogService.findWithUserById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") long id,
                                                         @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok().body(new ArticleResponse(updatedArticle));
    }
}