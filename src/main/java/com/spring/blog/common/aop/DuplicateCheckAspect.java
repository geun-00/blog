package com.spring.blog.common.aop;

import com.spring.blog.exception.duplicate.EmailDuplicateException;
import com.spring.blog.exception.duplicate.NicknameDuplicateException;
import com.spring.blog.exception.duplicate.PhoneNumberDuplicateException;
import com.spring.blog.service.DuplicateCheckService;
import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DuplicateCheckAspect {

    private final DuplicateCheckService duplicateCheckService;

    @Before(value = "Pointcuts.formUser(request)", argNames = "request")
    public void checkDuplicateFormUser(FormAddUserServiceRequest request) {

        checkEmail(request.email());
        checkPhoneNumber(request.phoneNumber());
        checkNickname(request.nickname());
    }

    @Before(value = "Pointcuts.oauthUser(request, email)", argNames = "request,email")
    public void checkDuplicateOauthUser(OAuthAddUserServiceRequest request, String email) {

        checkPhoneNumber(request.phoneNumber());
        checkNickname(request.nickname());
    }

    private void checkEmail(String request) {
        if (duplicateCheckService.isEmailDuplicate(request)) {
            throw new EmailDuplicateException();
        }
    }

    private void checkPhoneNumber(String request) {
        if (duplicateCheckService.isPhoneNumberDuplicate(request)) {
            throw new PhoneNumberDuplicateException();
        }
    }

    private void checkNickname(String request) {
        if (duplicateCheckService.isNicknameDuplicate(request)) {
            throw new NicknameDuplicateException();
        }
    }
}