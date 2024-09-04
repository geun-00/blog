package com.spring.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserVerifyViewController {

    @GetMapping("/findEmail")
    public String findEmail() {
        return "phone-number-request";
    }
}