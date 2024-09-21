package com.spring.blog.controller.unit;

import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.controller.ApiControllerUnitTestSupport;
import com.spring.blog.controller.advice.ApiControllerAdvice;
import com.spring.blog.controller.api.UserApiController;
import com.spring.blog.controller.dto.request.EditUserRequest;
import com.spring.blog.controller.dto.request.FormAddUserRequest;
import com.spring.blog.controller.dto.request.NewPasswordRequest;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.service.dto.request.EditUserServiceRequest;
import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import com.spring.blog.service.dto.request.NewPasswordServiceRequest;
import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User API 단위 테스트")
public class UserApiControllerUnitTest extends ApiControllerUnitTestSupport {

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserApiController(userService, userMapper))
                .setCustomArgumentResolvers(
                        new CurrentUserArgumentResolver()
                )
                .setControllerAdvice(new ApiControllerAdvice())
                .build();
    }

    @DisplayName("POST /api/formUser 테스트")
    @Test
    void signup() throws Exception {

        // given
        FormAddUserRequest request = new FormAddUserRequest(
                "email@gmail.com",
                "password1357@",
                "agumon",
                "01022334455"
        );
        FormAddUserServiceRequest serviceRequest = new FormAddUserServiceRequest(
                request.email(),
                request.password(),
                request.nickname(),
                request.phoneNumber()
        );

        given(userMapper.toServiceRequest(request)).willReturn(serviceRequest);
        given(userService.save(serviceRequest)).willReturn(serviceRequest.nickname());

        // when
        mockMvc.perform(
                        post("/api/formUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("CREATED"))
                .andExpect(jsonPath("$.data").value(serviceRequest.nickname()));

        // then
        verify(userMapper, times(1)).toServiceRequest(request);
        verify(userService, times(1)).save(serviceRequest);
    }

    @DisplayName("POST /api/oauthUser 테스트")
    @Test
    @WithPrincipalUser
    void oauthSignup() throws Exception {

        // given
        OAuthAddUserRequest request = new OAuthAddUserRequest(
                "01012341234",
                "password1357@"
        );
        OAuthAddUserServiceRequest serviceRequest = new OAuthAddUserServiceRequest(
                request.nickname(),
                request.phoneNumber()
        );

        given(userMapper.toServiceRequest(request)).willReturn(serviceRequest);
        given(userService.updateOAuthUser(any(), anyString())).willReturn(serviceRequest.nickname());

        // when
        mockMvc.perform(
                        post("/api/oauthUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("CREATED"))
                .andExpect(jsonPath("$.data").value(serviceRequest.nickname()));

        // then
        verify(userMapper, times(1)).toServiceRequest(request);
        verify(userService, times(1)).updateOAuthUser(any(), anyString());
    }

    @DisplayName("PATCH /api/user 테스트")
    @Test
    @WithPrincipalUser
    void userEdit() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile",
                "image/png",
                "test content".getBytes()
        );
        EditUserRequest request = new EditUserRequest("nickname", file);
        EditUserServiceRequest serviceRequest = new EditUserServiceRequest(request.nickname(), request.file());

        given(userMapper.toServiceRequest(request)).willReturn(serviceRequest);
        doNothing().when(userService).editUser(any(), anyString());

        mockMvc.perform(
                        multipart("/api/user")
                                .file(file)
                                .param("nickname", request.nickname())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .with(req -> {
                                    req.setMethod(HttpMethod.PATCH.name());
                                    return req;
                                })
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("수정 완료"));

        verify(userService, times(1)).editUser(any(), anyString());
        verify(userMapper, times(1)).toServiceRequest(request);
    }

    @DisplayName("POST /api/user/newPassword 테스트")
    @Test
    void setNewPassword() throws Exception {

        NewPasswordRequest request = new NewPasswordRequest("test@email.com", "newPassword12@");
        NewPasswordServiceRequest serviceRequest = new NewPasswordServiceRequest(request.email(), request.newPassword());

        given(userMapper.toServiceRequest(request)).willReturn(serviceRequest);
        doNothing().when(userService).setNewPassword(any());

        mockMvc.perform(post("/api/user/newPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());


        verify(userMapper, times(1)).toServiceRequest(request);
        verify(userService, times(1)).setNewPassword(any());
    }

    @DisplayName("DELETE /api/user 테스트")
    @Test
    @WithPrincipalUser
    void deleteUser() throws Exception {

        doNothing().when(userService).deleteUser(any());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();
        MockCookie cookie = new MockCookie("JSESSIONID", session.getId());

        request.setSession(session);
        request.setCookies(cookie);

        MvcResult result = mockMvc.perform(delete("/api/user")
                        .cookie(new MockCookie("JSESSIONID", session.getId()))
                        .requestAttr("request", request)
                        .requestAttr("response", response))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(cookie().path("JSESSIONID","/"))
                .andExpect(cookie().maxAge("JSESSIONID",0))
                .andReturn();

        verify(userService, times(1)).deleteUser(any());

        HttpSession httpSession = result.getRequest().getSession(false);
        assertThat(httpSession).isNull();
    }
}
