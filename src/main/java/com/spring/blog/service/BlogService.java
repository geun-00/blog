package com.spring.blog.service;

import com.spring.blog.common.events.ArticleDeletedEvent;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.ArticleImages;
import com.spring.blog.domain.ArticleLikes;
import com.spring.blog.domain.User;
import com.spring.blog.mapper.ArticleMapper;
import com.spring.blog.repository.ArticleImagesRepository;
import com.spring.blog.repository.ArticleLikesRepository;
import com.spring.blog.repository.BlogQueryRepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.BulkInsertRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleResponse;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.service.dto.response.LikeResponse;
import com.spring.blog.service.dto.response.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final CommentRepository commentRepository;
    private final BlogQueryRepository blogQueryRepository;
    private final BulkInsertRepository bulkInsertRepository;
    private final ArticleLikesRepository articleLikesRepository;
    private final ArticleImagesRepository articleImagesRepository;

    private final ArticleMapper articleMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    @CacheEvict(value = "articles", allEntries = true)
    public ArticleResponse save(ArticleServiceRequest request, String email, String sessionId) {

        User user = findUserByEmail(email);

        Article savedArticle = blogRepository.save(articleMapper.toEntity(request, user));

        Set<Object> imageUrls = redisTemplate.opsForSet().members(sessionId);

        if (imageUrls != null && !imageUrls.isEmpty()) {

            List<ArticleImages> articleImages = imageUrls.stream()
                    .map(url -> (String) url)
                    .map(url -> articleMapper.toEntity(savedArticle, url))
                    .toList();

            bulkInsertRepository.saveArticleImages(articleImages);
        }

        return articleMapper.toArticleResponse(savedArticle);
    }

    @Transactional
    @PreAuthorize("@articleSecurity.isOwner(#articleId, authentication.name)")
    @CacheEvict(value = "articles", allEntries = true)
    public ArticleResponse update(ArticleServiceRequest request, @P("articleId") long articleId) {

        Article article = blogRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("not found article from : " + articleId));

        article.update(request.title(), request.content());

        return articleMapper.toArticleResponse(article);
    }

    @Transactional
    @PreAuthorize("@articleSecurity.isOwner(#articleId, authentication.name)")
    @CacheEvict(value = "articles", allEntries = true)
    public void delete(@P("articleId") long articleId) {
        Article article = blogRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("not found article from : " + articleId));

        List<ArticleImages> articleImages = articleImagesRepository.getArticleImagesByArticle(article);

        articleImagesRepository.deleteByArticleId(articleId);
        articleLikesRepository.deleteByArticleId(articleId);
        commentRepository.deleteByArticleId(articleId);

        blogRepository.delete(article);

        eventPublisher.publishEvent(new ArticleDeletedEvent(articleImages));
    }

    @Transactional
    public ArticleViewResponse getArticleAndIncreaseViews(Long id, String userKey) {

        Article foundArticle = blogRepository.findWithUserAndCommentsById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found : " + id));

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

        return articleMapper.toArticleViewResponse(foundArticle);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public int addLike(Long articleId, String email) {

        User user = findUserByEmail(email);

        Article article = blogRepository.findByIdWithLock(articleId).orElseThrow(
                () -> new EntityNotFoundException("not found article from " + articleId));

        if (!articleLikesRepository.existsByUserAndArticle(user, article)) {

            articleLikesRepository.save(articleMapper.toEntity(user, article));

            article.increaseLikes();
        }

        return article.getLikes();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public int deleteLike(Long articleId, String email) {

        User user = findUserByEmail(email);

        Article article = blogRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("not found article from " + articleId));

        ArticleLikes articleLikes = articleLikesRepository.findByUserAndArticle(user, article).orElseThrow(
                () -> new EntityNotFoundException(
                        "not found likes (userId : " + user.getId() + ") (articleId : " + article.getId() + ")")
        );

        articleLikesRepository.delete(articleLikes);

        article.decreaseLikes();

        return article.getLikes();
    }

    public PageResponse<ArticleListViewResponse> findAllByCond(ArticleSearchServiceRequest request, Pageable pageable) {

        Page<ArticleListViewResponse> articles = blogQueryRepository.findAllByCond(request, pageable);

        return getPageResponse(pageable, articles);
    }

    @Cacheable(value = "articles", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public PageResponse<ArticleListViewResponse> findAll(Pageable pageable) {

        Page<ArticleListViewResponse> articles = blogQueryRepository.findAll(pageable);

        return getPageResponse(pageable, articles);
    }

    public AddArticleViewResponse getAddArticleViewResponse(Long articleId) {
        Article article = blogRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("not found article from " + articleId));

        return articleMapper.toAddArticleViewResponse(article);
    }

    public LikeResponse isLiked(Long articleId, String email) {

        Article article = blogRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("not found article from " + articleId));

        if (!StringUtils.hasText(email)) {
            return articleMapper.toLikeResponse(false, article.getLikes());
        }

        User user = findUserByEmail(email);

        boolean isLiked = articleLikesRepository.existsByUserAndArticle(user, article);
        int likes = article.getLikes();

        return articleMapper.toLikeResponse(isLiked, likes);
    }

    private PageResponse<ArticleListViewResponse> getPageResponse(Pageable pageable, Page<ArticleListViewResponse> articles) {
        return PageResponse.<ArticleListViewResponse>withAll()
                .dataList(articles.getContent())
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalCount(articles.getTotalElements())
                .build();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("not found user from : " + email));
    }
}