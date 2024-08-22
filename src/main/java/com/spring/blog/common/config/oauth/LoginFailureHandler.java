package com.spring.blog.common.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "인증 실패";

        if (exception instanceof OAuth2AuthenticationException) {
            errorMessage = "이미 가입된 정보가 있습니다. 다른 소셜을 사용해주세요.";
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else if (exception instanceof SessionAuthenticationException) {
            errorMessage = "중복된 세션입니다.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else if (exception instanceof BadCredentialsException) {
            errorMessage = "이메일 또는 비밀번호를 찾을 수 없습니다.";
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}