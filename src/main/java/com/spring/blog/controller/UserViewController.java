package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.validator.AddUserValidator;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;
    private final AddUserValidator addUserValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(addUserValidator);
    }

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("addUserRequest", new AddUserRequest());
        return "signup";
    }

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
}