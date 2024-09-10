package com.spring.blog.common.Interceptors;

import com.spring.blog.service.file.FileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class FileCleanUpInterceptor implements HandlerInterceptor {

    private static final String CLEANUP_REQUIRED = "cleanup";

    private final FileService fileService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(CLEANUP_REQUIRED, false);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        boolean cleanup = (boolean) request.getAttribute(CLEANUP_REQUIRED);

        String method = request.getMethod();
        if (!method.equalsIgnoreCase("POST") && !method.equalsIgnoreCase("PUT")) {
            return;
        }

        String sessionId = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            sessionId = Arrays.stream(cookies)
                    .filter(cookie -> "JSESSIONID".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("");
        }

        if (StringUtils.hasText(sessionId)) {
            if (cleanup) {
                fileService.cleanupFiles(sessionId);
            } else {
                redisTemplate.delete(sessionId);
            }
        }
    }
}
