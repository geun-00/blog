package com.spring.blog.service.security;

import com.spring.blog.domain.Article;
import com.spring.blog.repository.BlogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ArticleSecurity {

    private final BlogRepository blogRepository;

    public boolean isOwner(Long articleId, String username) {
        Article article = blogRepository.findByIdWithUser(articleId).orElseThrow(
                () -> new EntityNotFoundException("not found article from " + articleId));

        String nickname = article.getUser().getNickname();

        return StringUtils.equals(username, nickname);
    }
}