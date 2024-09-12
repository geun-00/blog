package com.spring.blog.controller.api;

import com.spring.blog.dto.request.EmailRequest;
import com.spring.blog.dto.request.EmailVerifyCodeRequest;
import com.spring.blog.dto.request.PhoneNumberRequest;
import com.spring.blog.dto.request.SmsVerifyCodeRequest;
import com.spring.blog.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserVerifyApiController {

    private final VerificationService verificationService;

    @PostMapping("/verify/phoneNumber")
    public ApiResponse<String> verifyPhoneNumber(@Validated @RequestBody PhoneNumberRequest request) {

        verificationService.sendVerificationCodeBySms(request.getPhoneNumber());

        return ApiResponse.ok("인증번호 전송");
    }

    @PostMapping("/verify/code/sms")
    public ApiResponse<String> verifyCodeBySms(@Validated @RequestBody SmsVerifyCodeRequest request) {

        String foundEmail = verificationService.verifyCodeBySms(
                request.getVerificationCode(),
                request.getPhoneNumber()
        );

        return ApiResponse.ok(foundEmail);
    }

    @PostMapping("/verify/email")
    public ApiResponse<String> verifyEmail(@Validated @RequestBody EmailRequest request) {

        verificationService.sendVerificationCodeByEmail(request.getEmail());

        return ApiResponse.ok("인증번호 전송");
    }

    @PostMapping("/verify/code/email")
    public ApiResponse<Boolean> verifyCodeByEmail(@Validated @RequestBody EmailVerifyCodeRequest request) {

        boolean verification = verificationService.verifyCodeByEmail(
                request.getVerificationCode(),
                request.getEmail()
        );

        return ApiResponse.ok(verification);
    }
}