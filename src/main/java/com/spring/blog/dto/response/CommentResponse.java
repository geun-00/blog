package com.spring.blog.dto.response;

import com.spring.blog.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse{

    private Long id;
    private String author;
    private String comment;
    private LocalDateTime createdAt;
    private Boolean isAuthor;

    public CommentResponse(Comment comment, boolean isAuthor) {
        this.id = comment.getId();
        this.author = comment.getUser().getNickname();
        this.comment = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isAuthor = isAuthor;
    }

    public boolean isAuthor() {
        return isAuthor;
    }
}