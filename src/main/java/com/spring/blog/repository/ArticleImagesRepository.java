package com.spring.blog.repository;

import com.spring.blog.domain.ArticleImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImagesRepository extends JpaRepository<ArticleImages, Long> {
}