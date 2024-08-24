package com.spring.blog.repository;

import com.spring.blog.domain.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Article> findWithUserById(long id);
}