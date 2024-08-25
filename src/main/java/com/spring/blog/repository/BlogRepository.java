package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Article> findWithUserById(long id);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Article a " +
            "JOIN FETCH a.user " +
            "ORDER BY a.createdAt DESC")
    List<Article> findAllByCreatedAtDesc();
}