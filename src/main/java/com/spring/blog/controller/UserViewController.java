package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.dto.request.FormAddUserRequest;
import com.spring.blog.dto.request.EditUserRequest;
import com.spring.blog.dto.response.UserInfoResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("addUserRequest", new FormAddUserRequest());
        return "signup";
    }

    @GetMapping("/myPage")
    public String myPage(@RequestParam(value = "author", required = false) String nickname,
                         @CurrentUser Authentication authentication, Model model) {

        String targetName = StringUtils.hasText(nickname)
                ? nickname
                : getPrincipal(authentication).getUsername();

        UserInfoResponse response = userService.getUserInfo(targetName);

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