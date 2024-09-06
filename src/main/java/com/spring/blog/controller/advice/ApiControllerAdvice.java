package com.spring.blog.controller.advice;

import com.spring.blog.controller.ApiResponse;
import com.spring.blog.exception.duplicate.DuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleBindException (MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> errors = fieldErrors
                .stream()
                .map(fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage())
                .toList();

        log.error("errors = {}", errors);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "잘못된 입력을 받았습니다.",
                null);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public ApiResponse<Object> handleDuplicateException(DuplicateException ex) {

        log.error("데이터 중복 예외 발생");

        return ApiResponse.of(
                HttpStatus.CONFLICT,
                "가입되어 있는 정보가 있습니다.",
                ex.getMessage()
        );
    }
}