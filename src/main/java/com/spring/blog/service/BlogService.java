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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final BlogQueryRepository blogQueryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

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

        Article article = blogRepository.findById(id)
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

    @Transactional
    public Article getArticleAndIncreaseViews(Long id, String userKey) {

        Article foundArticle = findWithUserAndCommentsById(id);

        SetOperations<String, Object> so = redisTemplate.opsForSet();
        Set<Object> viewedArticles = so.members(userKey);

        //레디스에서 오늘 조회한 게시글 목록에 없을 때에만 조회수 증가
        if (viewedArticles == null || !viewedArticles.contains(String.valueOf(id))) {

            foundArticle.increaseViews();
            so.add(userKey, String.valueOf(id));

            //TTL 설정, 오늘 자정까지
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextDay = now.toLocalDate().plusDays(1).atStartOfDay();
            redisTemplate.expire(userKey, Duration.between(now, nextDay));
        }

        return foundArticle;
    }

    public PageResponse<ArticleListViewResponse> findAllByCond(ArticleSearchRequest request, Pageable pageable) {

        Page<ArticleListViewResponse> articles = blogQueryRepository.findAllByCond(request, pageable);

        return getPageResponse(pageable, articles);
    }

    public PageResponse<ArticleListViewResponse> findAll(Pageable pageable) {

        Page<ArticleListViewResponse> articles = blogQueryRepository.findAll(pageable);

        return getPageResponse(pageable, articles);
    }

    public Article findWithUserAndCommentsById(Long id) {
        return blogRepository.findWithUserAndCommentsById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + id));
    }

    public Long countUserArticles(Long userId) {
        return blogRepository.countByUserId(userId);
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + id));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
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