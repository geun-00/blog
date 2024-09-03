package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.ArticleLikes;
import com.spring.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Long> {

    boolean existsByUserAndArticle(User user, Article article);
}