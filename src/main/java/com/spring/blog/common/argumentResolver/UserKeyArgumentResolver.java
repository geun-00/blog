package com.spring.blog.common.argumentResolver;

import com.spring.blog.common.annotation.UserKey;
import com.spring.blog.common.converters.utils.CookieUtil;
import com.spring.blog.model.PrincipalUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

public class UserKeyArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) &&
                parameter.hasParameterAnnotation(UserKey.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String userKey = null;
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
            userKey = principalUser.providerUser().getId();
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("USER_KEY".equals(cookie.getName())) {
                        userKey = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (!StringUtils.hasText(userKey)) {
            userKey = UUID.randomUUID().toString();
            CookieUtil.addCookie(response, "USER_KEY", userKey, 60 * 60 * 24);
        }

        return userKey;
    }
}