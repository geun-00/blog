package com.spring.blog.service.security;

import com.spring.blog.domain.Comment;
import com.spring.blog.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
@RequiredArgsConstructor
public class CommentSecurity {

    private final CommentRepository commentRepository;

    public boolean isOwner(Long commentId, String username) {
        Comment comment = commentRepository.findByIdWithUser(commentId).orElseThrow(
                () -> new EntityNotFoundException("not found comment from : " + commentId));

        String nickname = comment.getUser().getNickname();

        return StringUtils.equals(username, nickname);
    }
}