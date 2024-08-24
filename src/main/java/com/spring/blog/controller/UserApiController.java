package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(@CurrentUser Authentication authentication,
                                           HttpServletRequest request, HttpServletResponse response) {

        //사용자 제거
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        userService.deleteUser(principalUser.providerUser().getEmail());

        //세션, 쿠키 무효화
        invalidateSessionAndCookie(request, response);

        return ResponseEntity.ok().build();
    }

    private void invalidateSessionAndCookie(HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate();

        for (Cookie cookie : request.getCookies()) {
            if ("JSESSIONID".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }
}