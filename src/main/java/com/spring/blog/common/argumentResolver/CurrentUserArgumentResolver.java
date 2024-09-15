package com.spring.blog.common.argumentResolver;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.model.PrincipalUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PrincipalUser.class) &&
                parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public PrincipalUser resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        if (authentication == null || !(authentication.getPrincipal() instanceof PrincipalUser)) {
            if (pathMatcher.match("/api/articles/**/liked", request.getRequestURI())) {
                return null;
            }
            throw new EntityNotFoundException("현재 사용자를 찾을 수 없습니다.");
        }

        return (PrincipalUser) authentication.getPrincipal();
    }
}