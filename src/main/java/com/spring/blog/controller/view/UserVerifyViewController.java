package com.spring.blog.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserVerifyViewController {

    @GetMapping("/findEmail")
    public String findEmail() {
        return "phone-number-request";
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "email-request";
    }
}