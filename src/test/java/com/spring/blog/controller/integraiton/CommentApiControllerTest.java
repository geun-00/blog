package com.spring.blog.controller.integraiton;

import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.controller.ApiControllerIntegrationTestSupport;
import com.spring.blog.controller.dto.request.CommentRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("댓글 API 통합 테스트")
@Transactional
public class CommentApiControllerTest extends ApiControllerIntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("POST /api/comments/{articleId} 테스트, 정상 흐름")
    @Test
    @WithPrincipalUser
    void addComment() throws Exception {
        // given
        Long articleId = 1L;
        CommentRequest request = new CommentRequest("content");

        // when
        // then
        mockMvc.perform(post("/api/comments/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.comment").value(request.content()));
    }

    @DisplayName("POST /api/comments/{articleId} 테스트, 예외 흐름(null 체크)")
    @Test
    @WithPrincipalUser
    void addCommentValidateEx() throws Exception {
        // given
        Long articleId = 1L;
        CommentRequest request = new CommentRequest("");

        // when
        // then
        mockMvc.perform(post("/api/comments/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("PUT /api/comments/{commentId} 테스트")
    @Test
    @WithPrincipalUser
    void editComment() throws Exception {

        // given
        Long commentId = saveComment();

        CommentRequest editRequest = new CommentRequest("edit comment");

        // when
        // then
        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("PUT /api/comments/{commentId} 테스트, 예외 흐름(null 체크)")
    @Test
    @WithPrincipalUser
    void editCommentValidateEx() throws Exception {

        // given
        Long commentId = saveComment();

        CommentRequest editRequest = new CommentRequest("");

        // when
        // then
        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("DELETE /api/comments/{commentId} 테스트")
    @Test
    @WithPrincipalUser
    void deleteComment() throws Exception {

        // given
        Long commentId = saveComment();

        // when
        // then
        mockMvc.perform(delete("/api/comments/{commentId}", commentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    private Long saveComment() {
        User user = userRepository.findById(1L).get();
        Article article = blogRepository.findById(1L).get();

        return commentRepository.save(Comment.builder()
                .article(article)
                .user(user)
                .content("comment content")
                .build()).getId();
    }
}
