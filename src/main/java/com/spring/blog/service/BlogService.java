package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.dto.AddArticleRequest;
import com.spring.blog.dto.UpdateArticleRequest;
import com.spring.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
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
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id));

        authorizeArticleAuthor(article);

        blogRepository.delete(article);
    }

    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id));

        authorizeArticleAuthor(article);

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    //게시글을 작성한 사용자인지 확인
    private void authorizeArticleAuthor(Article article) {

        String userName = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
