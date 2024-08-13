package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.dto.AddArticleRequest;
import com.spring.blog.dto.UpdateArticleRequest;
import com.spring.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    public Article update(long id, UpdateArticleRequest request) {
        Article article = findById(id);

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
