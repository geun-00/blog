package com.spring.blog.controller;

import com.spring.blog.controller.validator.AddUserValidator;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;
    private final AddUserValidator addUserValidator;

    @PostMapping("/user")
    public String signup(@Validated @ModelAttribute AddUserRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        userService.save(request);

        return "redirect:/login";
    }

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(addUserValidator);
    }
}