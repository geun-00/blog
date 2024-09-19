package com.spring.blog.controller.unit;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.controller.ViewControllerUnitTestSupport;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.controller.view.UserViewController;
import com.spring.blog.service.dto.response.UserInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("사용자 뷰 단위 테스트")
public class UserViewControllerUnitTest extends ViewControllerUnitTestSupport {

    @DisplayName("/login 테스트")
    @TestFactory
    Collection<DynamicTest> login() {

        return List.of(
                DynamicTest.dynamicTest("정상적으로 로그인 되었을 때", () -> {

                    mockMvc.perform(get("/login"))
                            .andExpect(status().isOk())
                            .andExpect(view().name("login"))
                            .andExpect(model().attributeDoesNotExist("error", "exception"))
                            .andDo(print());
                }),

                DynamicTest.dynamicTest("다른 소셜로 이미 가입된 정보가 있을 때 예외 메시지 검증", () -> {

                    String errorMessage = "이미 가입된 정보가 있습니다. 다른 소셜을 사용해주세요.";

                    mockMvc.perform(
                                    get("/login")
                                            .param("error", "true")
                                            .param("exception", errorMessage)
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("login"))
                            .andExpect(model().attributeExists("error", "exception"))
                            .andExpect(model().attribute("error", "true"))
                            .andExpect(model().attribute("exception", errorMessage))
                            .andDo(print());
                }),

                DynamicTest.dynamicTest("중복 세션일 때 예외 메시지 검증", () -> {

                    String errorMessage = "중복된 세션입니다.";

                    mockMvc.perform(
                                    get("/login")
                                            .param("error", "true")
                                            .param("exception", errorMessage)
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("login"))
                            .andExpect(model().attributeExists("error", "exception"))
                            .andExpect(model().attribute("error", "true"))
                            .andExpect(model().attribute("exception", errorMessage))
                            .andDo(print());
                }),

                DynamicTest.dynamicTest("가입된 정보가 없을 때 예외 메시지 검증", () -> {

                    String errorMessage = "이메일 또는 비밀번호를 찾을 수 없습니다.";

                    mockMvc.perform(
                                    get("/login")
                                            .param("error", "true")
                                            .param("exception", errorMessage)
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("login"))
                            .andExpect(model().attributeExists("error", "exception"))
                            .andExpect(model().attribute("error", "true"))
                            .andExpect(model().attribute("exception", errorMessage))
                            .andDo(print());
                })
        );
    }

    @DisplayName("/oauth 테스트, 빈 모델과 함께 뷰로 이동한다.")
    @Test
    void oauthSignup() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/oauth")
                                .with(oauth2Login())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("oauthSignup"))
                .andExpect(model().attributeExists("addUserRequest"))
                .andDo(print())
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();

        Object request = model.get("addUserRequest");
        assertThat(request).isInstanceOf(OAuthAddUserRequest.class);

        OAuthAddUserRequest oAuthAddUserRequest = (OAuthAddUserRequest) request;
        assertThat(oAuthAddUserRequest.nickname()).isNull();
        assertThat(oAuthAddUserRequest.phoneNumber()).isNull();
    }

    @DisplayName("/userPage 테스트")
    @Test
    void userPage() throws Exception {

        UserInfoResponse response = EasyRandomFactory.createUserInfoResponse();

        given(userService.getUserInfo(any(String.class))).willReturn(response);

        mockMvc.perform(get("/userPage")
                        .param("username", anyString())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("userInfo"))
                .andExpect(model().attributeExists("user"))
                .andDo(print());
    }

    @DisplayName("/myPage 테스트")
    @Test
    @WithPrincipalUser
    void myPage() throws Exception {

        UserInfoResponse response = EasyRandomFactory.createUserInfoResponse();

        mockMvc = MockMvcBuilders.standaloneSetup(new UserViewController(userService))
                .setCustomArgumentResolvers(new CurrentUserArgumentResolver())
                .build();

        given(userService.getUserInfo(any(String.class))).willReturn(response);

        mockMvc.perform(get("/myPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("userInfo"))
                .andExpect(model().attributeExists("user"))
                .andDo(print());
    }
}
