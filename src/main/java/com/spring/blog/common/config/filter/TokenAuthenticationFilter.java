package com.spring.blog.common.config.filter;

import com.spring.blog.common.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final static String[] excludePaths = new String[]{
            "/login",
            "/signup",
            "/css/**",
            "/js/**",
            "/images/**",
            "/webjars/**",
            "/favicon.*",
            "/h2-console/**"
    };
    private final static String TOKEN_PREFIX = "Bearer ";
    private final static String HEADER_AUTHORIZATION = "Authorization";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (PatternMatchUtils.simpleMatch(excludePaths, requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        //요청 헤더 Authorization 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        //접두사 제거
        String token = getAccessToken(authorizationHeader);

        //유효한 토큰일 때만 인증 정보 설정
        if (token != null && tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}