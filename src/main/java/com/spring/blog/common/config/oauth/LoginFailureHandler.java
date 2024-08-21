package com.spring.blog.common.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "인증 실패";

        if (exception instanceof OAuth2AuthenticationException) {
            errorMessage = "이미 가입된 정보가 있습니다. 다른 소셜을 사용해주세요.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "잘못된 정보입니다.";
        }

        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}