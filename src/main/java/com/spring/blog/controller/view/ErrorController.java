package com.spring.blog.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/500")
    public String error() {
        return "error/500";
    }
}