package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.ArticleLikes;
import com.spring.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Long> {

    boolean existsByUserAndArticle(User user, Article article);

    Optional<ArticleLikes> findByUserAndArticle(User user, Article article);

    @Modifying
    @Query("DELETE FROM ArticleLikes a WHERE a.article.id = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Query("DELETE FROM ArticleLikes a WHERE a.article IN :articles")
    void deleteByArticles(@Param("articles") List<Article> articles);

    @Modifying
    @Query("DELETE FROM ArticleLikes a WHERE a.user = :user")
    void deleteByUser(@Param("user") User user);
}