package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.dto.request.EditUserRequest;
import com.spring.blog.dto.response.UserInfoResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "login";
    }

    @GetMapping("/oauth")
    public String oauthSignup(Model model) {
        model.addAttribute("addUserRequest", new OAuthAddUserRequest());
        return "oauthSignup";
    }

    @GetMapping("/userPage")
    public String myPage(@RequestParam("username") String username,
                         Model model) {

        UserInfoResponse response = userService.getUserInfo(username);

        model.addAttribute("user", response);

        return "myPage";
    }

    @GetMapping("/myPage")
    public String myPage(@CurrentUser Authentication authentication,
                         Model model) {

        PrincipalUser principalUser = getPrincipal(authentication);
        String username = principalUser.getUsername();

        UserInfoResponse response = userService.getUserInfo(username);

        model.addAttribute("user", response);

        return "myPage";
    }

    @GetMapping("/myPage/edit")
    public String editProfile(Model model) {

        model.addAttribute("editUserRequest", new EditUserRequest());

        return "myPageEdit";
    }

    private PrincipalUser getPrincipal(Authentication authentication) {
        return (PrincipalUser) authentication.getPrincipal();
    }
}