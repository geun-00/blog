package com.spring.blog.controller.integraiton;

import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.controller.ApiControllerIntegrationTestSupport;
import com.spring.blog.controller.dto.request.FormAddUserRequest;
import com.spring.blog.controller.dto.request.NewPasswordRequest;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.exception.duplicate.DuplicateException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User API 통합 테스트")
@Transactional
public class UserApiControllerTest extends ApiControllerIntegrationTestSupport {

    private static Stream<Arguments> editUserData() {
        return Stream.of(
                Arguments.of("1",
                        new MockMultipartFile("file", "test1.png", "image/png", "test content 1".getBytes()),
                        "nickname 유효성 체크"),
                Arguments.of("looooooooooooooongNickname",
                        new MockMultipartFile("file", "test1.png", "image/png", "test content 1".getBytes()),
                        "nickname 유효성 체크"),
                Arguments.of("nickname3",
                        new MockMultipartFile("file", "test3.txt", "text/plain", "test content 3".getBytes()),
                        "file 유효성 체크")
        );
    }

    @DisplayName("POST /api/formUser 테스트, 정상 흐름")
    @Test
    void signup() throws Exception {

        FormAddUserRequest request = new FormAddUserRequest(
                "email@gmail.com",
                "password1357@",
                "agumon",
                "01022334455"
        );

        // when
        mockMvc.perform(post("/api/formUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("CREATED"))
                .andExpect(jsonPath("$.data").value(request.nickname()));
    }

    @DisplayName("POST /api/formUser 테스트, 예외 흐름(유효성 검증 테스트)")
    @ParameterizedTest(name = "{4}")
    @CsvSource({
            "'', password1357@, agumon, 01022334455, email null 체크",
            "wrongEmail, password1357@, agumon, 01022334455, email 유효성 체크",
            "jky00914@naver.com, '', agumon, 01022334455, password null 체크",
            "jky00914@naver.com, wrongPassword, agumon, 01022334455, password 유효성 체크",
            "jky00914@naver.com, password1357@, '', 01022334455, nickname null 체크",
            "jky00914@naver.com, password1357@, '1', 01022334455, nickname 유효성 체크",
            "jky00914@naver.com, password1357@, 'looooooooooooongNickname', 01022334455, nickname 유효성 체크",
            "jky00914@naver.com, password1357@, 'agumon', '', phoneNumber null 체크",
            "jky00914@naver.com, password1357@, 'agumon', '021342334', phoneNumber 유효성 체크",
    })
    void signupWithValidation(String email, String password, String nickname, String phoneNumber, String message) throws Exception {

        // given
        FormAddUserRequest request = new FormAddUserRequest(email, password, nickname, phoneNumber);

        // when
        MvcResult result = mockMvc.perform(
                        post("/api/formUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        // then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("POST /api/formUser 테스트, 예외 흐름(중복 검사 테스트)")
    @ParameterizedTest(name = "{4}")
    @CsvSource({
            "user@test.com, password1357@, agumon, 01013572468, email 중복 테스트",
            "jky00914@naver.com, password1357@, nickname0, 01013572468, nickname 중복 테스트",
            "jky00914@naver.com, password1357@, agumon, 01012341234, phoneNumber 중복 테스트"
    })
    void signupWithDuplicateEx(String email, String password, String nickname, String phoneNumber, String message) throws Exception {

        FormAddUserRequest request = new FormAddUserRequest(
                email, password, nickname, phoneNumber
        );

        // when
        MvcResult result = mockMvc.perform(
                        post("/api/formUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("가입되어 있는 정보가 있습니다."))
                .andReturn();

        // then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(DuplicateException.class);
    }

    @DisplayName("POST /api/oauthUser 테스트, 정상 흐름")
    @Test
    @WithPrincipalUser
    void oauthSignup() throws Exception {

        // given
        OAuthAddUserRequest request = new OAuthAddUserRequest(
                "01022334455",
                "agumon"
        );

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
                .andExpect(jsonPath("$.data").value(request.nickname()));
    }

    @DisplayName("POST /api/oauthUser 테스트, 예외 흐름(유효성 검증 테스트)")
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "'', 01022334455, nickname null 체크",
            "1, 01022334455, nickname 유효성 체크",
            "looooooooooooongNickname, 01022334455, nickname 유효성 체크",
            "agumon, '', phoneNumber null 체크",
            "agumon, 021342334, phoneNumber 유효성 체크",
    })
    @WithPrincipalUser
    void oauthSignupWithValidation(String nickname, String phoneNumber, String message) throws Exception {

        // given
        OAuthAddUserRequest request = new OAuthAddUserRequest(
                phoneNumber, nickname
        );

        // when
        MvcResult result = mockMvc.perform(
                        post("/api/oauthUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        // then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("POST /api/oauthUser 테스트, 예외 흐름(중복 검사 테스트)")
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "nickname0, 01013572468, nickname 중복 체크",
            "agumon, 01012341234, phoneNumber 중복 체크"
    })
    @WithPrincipalUser
    void oauthSignupWithDuplicateEx(String nickname, String phoneNumber, String message) throws Exception {

        OAuthAddUserRequest request = new OAuthAddUserRequest(
                phoneNumber, nickname
        );

        // when
        MvcResult result = mockMvc.perform(
                        post("/api/oauthUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("가입되어 있는 정보가 있습니다."))
                .andReturn();

        // then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(DuplicateException.class);
    }

    @DisplayName("PATCH /api/user 테스트, 정상 흐름")
    @Test
    @WithPrincipalUser
    void userEdit() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile",
                "image/png",
                "test content".getBytes()
        );

        String changeNickname = "nickname";

        mockMvc.perform(multipart("/api/user")
                        .file(file)
                        .param("nickname", changeNickname)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod(HttpMethod.PATCH.name());
                            return req;
                        }))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("수정 완료"));
    }

    @DisplayName("PATCH /api/user 테스트, 예외 흐름(중복 검사 테스트)")
    @Test
    @WithPrincipalUser
    void userEditDuplicateEx() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile",
                "image/png",
                "test content".getBytes()
        );
        String changeNickname = "nickname1";

        MvcResult result = mockMvc.perform(multipart("/api/user")
                        .file(file)
                        .param("nickname", changeNickname)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod(HttpMethod.PATCH.name());
                            return req;
                        }))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("가입되어 있는 정보가 있습니다."))
                .andExpect(jsonPath("$.data").value("닉네임이 이미 등록되어 있습니다."))
                .andReturn();

        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(DuplicateException.class);
    }

    @DisplayName("PATCH /api/user 테스트, 예외 흐름(유효성 검증 테스트)")
    @ParameterizedTest(name = "{2}")
    @MethodSource("editUserData")
    @WithPrincipalUser
    void userEditWithValidation(String changeNickname, MockMultipartFile file, String message) throws Exception {

        MvcResult result = mockMvc.perform(multipart("/api/user")
                        .file(file)
                        .param("nickname", changeNickname)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod(HttpMethod.PATCH.name());
                            return req;
                        }))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("POST /api/user/newPassword 테스트, 정상 흐름")
    @Test
    @WithPrincipalUser
    void setNewPassword() throws Exception {

        NewPasswordRequest request = new NewPasswordRequest("user@test.com", "newPassword12@");

        mockMvc.perform(post("/api/user/newPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("POST /api/user/newPassword 테스트, 예외 흐름(유효성 검증 테스트)")
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "'', password1357@, email null 체크",
            "wrongEmail, password1357@, email 유효성 체크",
            "wrongEmail, '', password null 체크",
            "test@email.com, wrongPassword, password 유효성 체크"
    })
    @WithMockUser
    void setNewPasswordWithValidation(String email, String password, String message) throws Exception {

        NewPasswordRequest request = new NewPasswordRequest(
                email, password);

        MvcResult result = mockMvc.perform(post("/api/user/newPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("DELETE /api/user 테스트")
    @Test
    @WithPrincipalUser
    void deleteUser() throws Exception {

        MvcResult result = mockMvc.perform(delete("/api/user")
                        .cookie(new Cookie("JSESSIONID", "session-id")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(cookie().path("JSESSIONID", "/"))
                .andExpect(cookie().maxAge("JSESSIONID", 0))
                .andReturn();

        HttpSession httpSession = result.getRequest().getSession(false);
        assertThat(httpSession).isNull();
    }
}