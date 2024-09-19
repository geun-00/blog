package com.spring.blog.controller.view;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.service.dto.response.UserInfoResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
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
        model.addAttribute("addUserRequest", new OAuthAddUserRequest(null, null));
        return "oauthSignup";
    }

    @GetMapping("/userPage")
    public String myPage(@RequestParam("username") String username,
                         Model model) {

        UserInfoResponse response = userService.getUserInfo(username);

        model.addAttribute("user", response);

        return "userInfo";
    }

    @GetMapping("/myPage")
    public String myPage(@CurrentUser PrincipalUser principalUser,
                         Model model) {

        String username = principalUser.getUsername();

        UserInfoResponse response = userService.getUserInfo(username);

        model.addAttribute("user", response);

        return "userInfo";
    }
}