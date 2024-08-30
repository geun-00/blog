package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.dto.CommentRequest;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment addComment(CommentRequest request, String email) {

        Article foundArticle = blogRepository.findWithUserById(request.getArticleId()).orElseThrow(
                () -> new EntityNotFoundException("not found : " + request.getArticleId()));

        User foundUser = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("not found : " + email));

        return commentRepository.save(Comment.builder()
                .content(request.getComment())
                .user(foundUser)
                .article(foundArticle)
                .build());
    }
}