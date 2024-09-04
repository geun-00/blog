package com.spring.blog.controller;

import com.spring.blog.dto.request.PhoneNumberRequest;
import com.spring.blog.dto.request.VerifyCodeRequest;
import com.spring.blog.exception.SmsException;
import com.spring.blog.exception.VerificationException;
import com.spring.blog.service.SmsService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserVerifyApiController {

    private final SmsService smsService;

    @PermitAll
    @PostMapping("/verify/phoneNumber")
    public ApiResponse<String> verifyPhoneNumber(@Validated @RequestBody PhoneNumberRequest request) {
        smsService.sendVerificationCode(request.getPhoneNumber());

        return ApiResponse.ok("인증번호 전송");
    }

    @PermitAll
    @PostMapping("/verify/code")
    public ApiResponse<String> verifyCode(@Validated @RequestBody VerifyCodeRequest request) {

        String foundEmail = smsService.verityCode(request);

        return ApiResponse.ok(foundEmail);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VerificationException.class)
    public ApiResponse<Object> handleVerificationException(VerificationException ex) {

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SmsException.class)
    public ApiResponse<Object> handleSmsException(SmsException ex) {

        return ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                null);
    }
}