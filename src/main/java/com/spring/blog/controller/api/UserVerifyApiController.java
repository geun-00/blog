package com.spring.blog.controller.api;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.controller.dto.request.EmailRequest;
import com.spring.blog.controller.dto.request.EmailVerifyCodeRequest;
import com.spring.blog.controller.dto.request.PhoneNumberRequest;
import com.spring.blog.controller.dto.request.SmsVerifyCodeRequest;
import com.spring.blog.exception.EmailSendException;
import com.spring.blog.exception.SmsException;
import com.spring.blog.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserVerifyApiController {

    private final VerificationService verificationService;

    @PostMapping("/verify/phoneNumber")
    public CompletableFuture<ApiResponse<String>> verifyPhoneNumber(@Validated @RequestBody PhoneNumberRequest request) {

        CompletableFuture<Boolean> future = verificationService.sendVerificationCodeBySms(request.phoneNumber());

        return future
                .thenApply(result -> {
                    if (result) {
                        return ApiResponse.ok("인증번호 전송");
                    } else {
                        throw new SmsException("인증번호 전송 실패");
                    }
                })
                .exceptionally(ex -> {
                    throw new SmsException("비동기 작업 중 오류 발생", ex);
                });
    }

    @PostMapping("/verify/code/sms")
    public ApiResponse<String> verifyCodeBySms(@Validated @RequestBody SmsVerifyCodeRequest request) {

        String foundEmail = verificationService.verifyCodeBySms(
                request.verificationCode(),
                request.phoneNumber()
        );

        return ApiResponse.ok(foundEmail);
    }

    @PostMapping("/verify/email")
    public CompletableFuture<ApiResponse<String>> verifyEmail(@Validated @RequestBody EmailRequest request) {

        CompletableFuture<Boolean> future = verificationService.sendVerificationCodeByEmail(request.email());

        return future
                .thenApply(result -> {
                    if (result) {
                        return ApiResponse.ok("인증번호 전송");
                    } else {
                        throw new EmailSendException("인증번호 전송 실패");
                    }
                })
                .exceptionally(ex -> {
                    throw new EmailSendException("비동기 작업 중 오류 발생", ex);
                });
    }

    @PostMapping("/verify/code/email")
    public ApiResponse<String> verifyCodeByEmail(@Validated @RequestBody EmailVerifyCodeRequest request) {

        SocialType socialType = verificationService.verifyCodeByEmail(
                request.verificationCode(),
                request.email()
        );

        return ApiResponse.ok(socialType.getSocialName());
    }
}