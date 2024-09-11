package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.domain.Comment;
import com.spring.blog.controller.dto.request.CommentRequest;
import com.spring.blog.dto.response.CommentResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{articleId}")
    public ApiResponse<CommentResponse> addComment(@PathVariable("articleId") Long articleId,
                                                   @Validated @RequestBody CommentRequest request,
                                                   @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        String email = principalUser.providerUser().getEmail();
        CommentResponse response = commentService.addComment(articleId, request.toServiceRequest(), email);

        return ApiResponse.ok(response);
    }

    @PutMapping("/comments/{commentId}")
    public ApiResponse<Void> editComment(@PathVariable("commentId") Long commentId,
                                         @Validated @RequestBody CommentRequest request) {

        commentService.editComment(commentId, request.toServiceRequest());

        return ApiResponse.ok(null);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        Comment comment = commentService.deleteComment(commentId);


        return ResponseEntity.ok().build();
    }
}