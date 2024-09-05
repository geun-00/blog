package com.spring.blog.controller;

import com.spring.blog.dto.request.EmailRequest;
import com.spring.blog.dto.request.EmailVerifyCodeRequest;
import com.spring.blog.dto.request.PhoneNumberRequest;
import com.spring.blog.dto.request.SmsVerifyCodeRequest;
import com.spring.blog.exception.SmsException;
import com.spring.blog.exception.VerificationException;
import com.spring.blog.service.VerificationService;
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

    private final VerificationService verificationService;

    @PermitAll
    @PostMapping("/verify/phoneNumber")
    public ApiResponse<String> verifyPhoneNumber(@Validated @RequestBody PhoneNumberRequest request) {
        verificationService.sendVerificationCodeBySms(request.getPhoneNumber());

        return ApiResponse.ok("인증번호 전송");
    }

    @PermitAll
    @PostMapping("/verify/code/sms")
    public ApiResponse<String> verifyCodeBySms(@Validated @RequestBody SmsVerifyCodeRequest request) {

        String foundEmail = verificationService.verifyCodeBySms(request);

        return ApiResponse.ok(foundEmail);
    }

    @PostMapping("/verify/email")
    public ApiResponse<String> verifyEmail(@Validated @RequestBody EmailRequest request) {
        verificationService.sendVerificationCodeByEmail(request.getEmail());
        return ApiResponse.ok("인증번호 전송");
    }

    @PostMapping("/verify/code/email")
    public ApiResponse<Boolean> verifyCodeByEmail(@Validated @RequestBody EmailVerifyCodeRequest request) {

        boolean verification = verificationService.verifyCodeByEmail(request);

        return ApiResponse.ok(verification);
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