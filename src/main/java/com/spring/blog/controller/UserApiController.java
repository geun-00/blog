package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.dto.request.EditUserRequest;
import com.spring.blog.controller.dto.request.FormAddUserRequest;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.dto.request.NewPasswordRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import com.spring.blog.service.oauth.unlink.OAuth2UnlinkService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final OAuth2UnlinkService oAuth2UnlinkService;

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

    @PatchMapping("/user")
    public ApiResponse<String> userEdit(@Validated @ModelAttribute EditUserRequest request,
                                                    @CurrentUser Authentication authentication) {

        PrincipalUser principalUser = getPrincipal(authentication);
        userService.editUser(request.toServiceRequest(), principalUser.providerUser().getEmail());

        return ApiResponse.ok("수정 완료");
    }

    @PostMapping("/user/newPassword")
    public ApiResponse<?> setNewPassword(@RequestBody NewPasswordRequest request) {
        userService.setNewPassword(request);

        return ApiResponse.ok(null);
    }

    @DeleteMapping("/user")
    public ApiResponse<Void> deleteUser(@CurrentUser Authentication authentication,
                                        HttpServletRequest request, HttpServletResponse response) {

        //사용자 제거
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        userService.deleteUser(principalUser.providerUser().getEmail(), authentication);

        //세션, 쿠키 무효화
        invalidateSessionAndCookie(request, response);

        return ApiResponse.ok(null);
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