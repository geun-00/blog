package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.domain.Comment;
import com.spring.blog.dto.CommentRequest;
import com.spring.blog.dto.CommentResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest commentRequest,
                                                      @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        String email = principalUser.providerUser().getEmail();
        Comment comment = commentService.addComment(commentRequest, email);

        return ResponseEntity.ok().body(new CommentResponse(comment));
    }
}