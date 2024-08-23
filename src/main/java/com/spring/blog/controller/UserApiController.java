package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.validator.AddUserValidator;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserApiController {

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(addUserValidator);
    }

    private final UserService userService;
    private final AddUserValidator addUserValidator;

    @PostMapping("/user")
    public String signup(@Validated @ModelAttribute AddUserRequest request, BindingResult bindingResult,
                         @CurrentUser Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        //폼 인증 가입
        if (authentication == null) {
            userService.save(request);
        }
        //OAuth 인증 가입, 별명만 재설정
        else {
            PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
            userService.updateNickname(request.getNickname(), principalUser.getEmail());
        }

        return "redirect:/login?success";
    }

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