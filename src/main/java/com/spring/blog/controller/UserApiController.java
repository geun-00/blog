package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.dto.request.FormAddUserRequest;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.domain.User;
import com.spring.blog.dto.request.EditUserRequest;
import com.spring.blog.dto.request.NewPasswordRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/formUser")
    public ApiResponse<String> signup(@Validated @RequestBody FormAddUserRequest request) {

        String username = userService.save(request.toServiceRequest());

        return ApiResponse.of(
                HttpStatus.CREATED,
                username
        );
    }

    @PostMapping("/oauthUser")
    public ApiResponse<String> oauthSignup(@Validated @RequestBody OAuthAddUserRequest request,
                                           @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = getPrincipal(authentication);
        String username = userService.updateOAuthUser(request.toServiceRequest(), principalUser.providerUser().getEmail());

        return ApiResponse.of(
                HttpStatus.CREATED,
                username
        );
    }

    @PostMapping("/user/edit")
    public String userEdit(@Validated @ModelAttribute EditUserRequest request, BindingResult bindingResult,
                           @CurrentUser Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "myPageEdit";
        }

        PrincipalUser principalUser = getPrincipal(authentication);
        User updatedUser = userService.editUser(principalUser.providerUser().getEmail(), request);

        updateContext(principalUser, authentication, updatedUser);

        return "redirect:/myPage";
    }

    private void updateContext(PrincipalUser principalUser, Authentication authentication, User user) {

        PrincipalUser updatedprincipalUser = principalUser.withUpdatedUser(user);

        Authentication newAuth = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            newAuth = new OAuth2AuthenticationToken(
                    updatedprincipalUser,
                    authentication.getAuthorities(),
                    updatedprincipalUser.providerUser().getClientRegistration().getRegistrationId()
            );
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            newAuth = new UsernamePasswordAuthenticationToken(
                    updatedprincipalUser,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
        }

        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(newAuth);
    }

    @PostMapping("/user/newPassword")
    public ApiResponse<?> setNewPassword(@RequestBody NewPasswordRequest request) {
        userService.setNewPassword(request);

        return ApiResponse.ok(null);
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

    private PrincipalUser getPrincipal(Authentication authentication) {
        return (PrincipalUser) authentication.getPrincipal();
    }
}