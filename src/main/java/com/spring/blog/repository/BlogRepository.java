package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Article> findWithUserById(long id);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    Page<Article> findAll(Pageable pageable);
}