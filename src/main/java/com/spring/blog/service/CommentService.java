package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.service.dto.response.CommentResponse;
import com.spring.blog.mapper.CommentMapper;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.dto.request.CommentServiceRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public CommentResponse addComment(Long articleId, CommentServiceRequest request, String email) {

        Article foundArticle = blogRepository.findByIdWithUser(articleId).orElseThrow(
                () -> new EntityNotFoundException("not found article from " + articleId));

        User foundUser = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("not found user from " + email));

        Comment savedCommend = commentRepository.save(commentMapper.toEntity(request, foundUser, foundArticle));

        boolean isAuthor = StringUtils.equals(foundUser.getNickname(), foundArticle.getUser().getNickname());

        return commentMapper.toResponse(savedCommend, isAuthor);
    }

    @Transactional
    @PreAuthorize("@commentSecurity.isOwner(#commentId, authentication.name)")
    public void editComment(@P("commentId") Long commentId, CommentServiceRequest request) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("not found comment from " + commentId));

        foundComment.editComment(request.content());
    }

    @Transactional
    @PreAuthorize("@commentSecurity.isOwner(#commentId, authentication.name)")
    public void deleteComment(@P("commentId") Long commentId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("not found comment : " + commentId));

        commentRepository.delete(foundComment);
    }
}