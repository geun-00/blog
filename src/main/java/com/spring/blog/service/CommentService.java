package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.dto.request.CommentRequest;
import com.spring.blog.dto.response.CommentResponse;
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
    public CommentResponse addComment(CommentRequest request, String email) {

        Article foundArticle = blogRepository.findById(request.getArticleId()).orElseThrow(
                () -> new EntityNotFoundException("not found : " + request.getArticleId()));

        User foundUser = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("not found : " + email));

        Comment savedCommend = commentRepository.save(Comment.builder()
                .content(request.getComment())
                .user(foundUser)
                .article(foundArticle)
                .build());

        boolean isAuthor = foundUser.getNickname().equals(foundArticle.getUser().getNickname());

        return new CommentResponse(savedCommend, isAuthor);
    }
}