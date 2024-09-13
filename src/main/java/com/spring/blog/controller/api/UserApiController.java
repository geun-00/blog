package com.spring.blog.controller.api;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.dto.request.EditUserRequest;
import com.spring.blog.controller.dto.request.FormAddUserRequest;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.controller.dto.request.NewPasswordRequest;
import com.spring.blog.mapper.UserMapper;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final UserMapper userMapper;

    @PostMapping("/formUser")
    public ApiResponse<String> signup(@Validated @RequestBody FormAddUserRequest request) {

        String username = userService.save(userMapper.toServiceRequest(request));

        return ApiResponse.of(
                HttpStatus.CREATED,
                username
        );
    }

    @PostMapping("/oauthUser")
    public ApiResponse<String> oauthSignup(@Validated @RequestBody OAuthAddUserRequest request,
                                           @CurrentUser PrincipalUser principalUser) {

        String username = userService.updateOAuthUser(
                userMapper.toServiceRequest(request),
                principalUser.providerUser().getEmail()
        );

        return ApiResponse.of(
                HttpStatus.CREATED,
                username
        );
    }

    @PatchMapping("/user")
    public ApiResponse<String> userEdit(@Validated @ModelAttribute EditUserRequest request,
                                        @CurrentUser PrincipalUser principalUser) {

        userService.editUser(
                userMapper.toServiceRequest(request),
                principalUser.providerUser().getEmail()
        );

        return ApiResponse.ok("수정 완료");
    }

    @PostMapping("/user/newPassword")
    public ApiResponse<?> setNewPassword(@RequestBody NewPasswordRequest request) {
        userService.setNewPassword(userMapper.toServiceRequest(request));

        return ApiResponse.ok(null);
    }

    @DeleteMapping("/user")
    public ApiResponse<Void> deleteUser(@CurrentUser PrincipalUser principalUser,
                                        HttpServletRequest request, HttpServletResponse response) {

        //사용자 제거
        userService.deleteUser(principalUser);

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
}