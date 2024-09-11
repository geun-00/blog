package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.dto.response.CommentResponse;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.dto.request.CommentServiceRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse addComment(Long articleId, CommentServiceRequest request, String email) {

        Article foundArticle = blogRepository.findByIdWithUser(articleId).orElseThrow(
                () -> new EntityNotFoundException("not found article from " + articleId));

        User foundUser = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("not found user from " + email));

        Comment savedCommend = commentRepository.save(Comment.builder()
                .content(request.getComment())
                .user(foundUser)
                .article(foundArticle)
                .build());

        boolean isAuthor = StringUtils.equals(foundUser.getNickname(), foundArticle.getUser().getNickname());

        return new CommentResponse(savedCommend, isAuthor);
    }

    @Transactional
    public void editComment(Long commentId, CommentServiceRequest request) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("not found comment from " + commentId));

        foundComment.editComment(request.getComment());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("not found comment : " + commentId));

        commentRepository.delete(foundComment);
    }
}