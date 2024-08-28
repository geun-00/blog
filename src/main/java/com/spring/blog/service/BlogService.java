package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.dto.AddArticleRequest;
import com.spring.blog.dto.ArticleListViewResponse;
import com.spring.blog.dto.ArticleSearchRequest;
import com.spring.blog.dto.PageResponse;
import com.spring.blog.dto.UpdateArticleRequest;
import com.spring.blog.repository.BlogQueryRepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final BlogQueryRepository blogQueryRepository;

    @Transactional
    public Article save(AddArticleRequest request, String email) {

        validationService.checkValid(request);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + email));
        Article article = request.toEntity();
        user.addArticle(article);

        return blogRepository.save(article);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {

        validationService.checkValid(request);

        Article article = blogRepository.findWithUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    @Transactional
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + id));

        blogRepository.delete(article);
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public PageResponse<ArticleListViewResponse> findAllByCond(ArticleSearchRequest request, Pageable pageable) {

        Page<ArticleListViewResponse> articles = blogQueryRepository
                .findAllByCond(request, pageable)
                .map(ArticleListViewResponse::new);

        return getPageResponse(pageable, articles);
    }

    public PageResponse<ArticleListViewResponse> findAll(Pageable pageable) {

        Page<ArticleListViewResponse> articles = blogRepository
                .findAll(pageable)
                .map(ArticleListViewResponse::new);

        return getPageResponse(pageable, articles);
    }

    public Article findWithUserById(Long id) {
        return blogRepository.findWithUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + id));
    }

    public Long countUserArticles(Long userId) {
        return blogRepository.countByUserId(userId);
    }

    private PageResponse<ArticleListViewResponse> getPageResponse(Pageable pageable, Page<ArticleListViewResponse> articles) {
        return PageResponse.<ArticleListViewResponse>withAll()
                .dataList(articles.getContent())
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalCount(articles.getTotalElements())
                .build();
    }
}