package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.user = :user")
    void deleteByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.article.id = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.article IN :articles")
    void deleteByArticles(@Param("articles") List<Article> articles);
}