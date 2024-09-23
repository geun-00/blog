package com.spring.blog.controller.unit;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.controller.ApiControllerUnitTestSupport;
import com.spring.blog.controller.api.CommentApiController;
import com.spring.blog.controller.dto.request.CommentRequest;
import com.spring.blog.service.dto.request.CommentServiceRequest;
import com.spring.blog.service.dto.response.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("댓글 API 단위 테스트")
public class CommentApiControllerUnitTest extends ApiControllerUnitTestSupport {

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentApiController(commentMapper, commentService))
                .setCustomArgumentResolvers(
                        new CurrentUserArgumentResolver()
                )
                .build();
    }

    @DisplayName("POST /api/comments/{articleId} 테스트")
    @Test
    @WithPrincipalUser
    void addComment() throws Exception {

        // given
        CommentRequest request = new CommentRequest("comment");
        CommentServiceRequest serviceRequest = new CommentServiceRequest(request.content());

        CommentResponse response = EasyRandomFactory.createCommentResponse();

        given(commentMapper.toServiceRequest(request)).willReturn(serviceRequest);
        given(commentService.addComment(anyLong(), any(), anyString())).willReturn(response);

        // when
        mockMvc.perform(post("/api/comments/{articleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // then
        verify(commentMapper, times(1)).toServiceRequest(request);
        verify(commentService, times(1)).addComment(anyLong(), any(), anyString());
    }

    @DisplayName("PUT /api/comments/{commentId} 테스트")
    @Test
    @WithPrincipalUser
    void editComment() throws Exception {

        // given
        CommentRequest request = new CommentRequest("comment");
        CommentServiceRequest serviceRequest = new CommentServiceRequest(request.content());

        given(commentMapper.toServiceRequest(request)).willReturn(serviceRequest);
        doNothing().when(commentService).editComment(anyLong(), any());

        // when
        mockMvc.perform(put("/api/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());

        // then
        verify(commentMapper, times(1)).toServiceRequest(request);
        verify(commentService, times(1)).editComment(anyLong(), any());
    }

    @DisplayName("DELETE /api/comments/{commentId} 테스트")
    @Test
    @WithPrincipalUser
    void deleteComment() throws Exception {

        // given
        doNothing().when(commentService).deleteComment(anyLong());

        // when
        mockMvc.perform(delete("/api/comments/{commentId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());

        // then
        verify(commentService, times(1)).deleteComment(anyLong());
    }
}
