package com.spring.blog.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ErrorMessage {

    private List<String> errors;
}