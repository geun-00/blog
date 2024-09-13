package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a " +
            "JOIN FETCH a.user " +
            "LEFT JOIN FETCH a.comments " +
            "WHERE a.id = :id")
    Optional<Article> findWithUserAndCommentsById(@Param("id") Long id);

    @EntityGraph(attributePaths = "user")
    Page<Article> findAll(Pageable pageable);

    @Query("SELECT a FROM Article a " +
            "JOIN FETCH a.user " +
            "WHERE a.id = :id")
    Optional<Article> findByIdWithUser(@Param("id") Long articleId);

    @Modifying
    @Query("DELETE FROM Article a WHERE a.user = :user")
    void deleteByUser(@Param("user") User user);

    List<Article> findByUser(User user);
}