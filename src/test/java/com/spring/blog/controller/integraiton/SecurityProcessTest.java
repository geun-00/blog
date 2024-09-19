package com.spring.blog.controller.integraiton;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.controller.ViewControllerIntegrationTestSupport;
import com.spring.blog.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DisplayName("시큐리티 인증 과정 테스트")
public class SecurityProcessTest extends ViewControllerIntegrationTestSupport {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("폼 로그인 시나리오")
    @TestFactory
    Collection<DynamicTest> formLoginTest() {

        userRepository.save(User.builder()
                .email("test@email.com")
                .password(passwordEncoder.encode("testPassword12@"))
                .nickname("nickname22")
                .phoneNumber("01098765432")
                .registrationId(SocialType.NONE)
                .build());

        return List.of(
                DynamicTest.dynamicTest("정상적인 로그인", () -> {

                    mockMvc.perform(formLogin("/loginProc")
                                    .user("email", "test@email.com")
                                    .password("testPassword12@")
                            )
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/articles"))
                            .andDo(print());
                }),

                DynamicTest.dynamicTest("잘못된 인증 시도", () -> {

                    MvcResult result = mockMvc.perform(formLogin("/loginProc")
                                    .user("email", "wrong@email.com")
                                    .password("wrongPassword")
                            )
                            .andExpect(status().is3xxRedirection())
                            .andDo(print())
                            .andReturn();

                    String redirectedUrl = result.getResponse().getRedirectedUrl();
                    assertThat(redirectedUrl).isNotNull();
                    String decodedRedirectUrl = URLDecoder.decode(redirectedUrl, StandardCharsets.UTF_8);

                    assertThat(decodedRedirectUrl).isEqualTo("/login?error=true&exception=이메일 또는 비밀번호를 찾을 수 없습니다.");
                })
        );
    }
}