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
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DuplicateCheckAspect {

    private final DuplicateCheckService duplicateCheckService;

    @Pointcut("@annotation(com.spring.blog.common.annotation.DuplicateCheck)")
    private void duplicateCheck(){}

    @Pointcut("duplicateCheck() && args(request)")
    private void formUser(FormAddUserServiceRequest request){}

    @Pointcut(value = "duplicateCheck() && args(request, email)", argNames = "request,email")
    private void oauthUser(OAuthAddUserServiceRequest request, String email){}

    @Before(value = "formUser(request)", argNames = "request")
    public void checkDuplicateFormUser(FormAddUserServiceRequest request) {

        if (duplicateCheckService.isEmailDuplicate(request.getEmail())) {
            throw new EmailDuplicateException();
        }
        if (duplicateCheckService.isPhoneNumberDuplicate(request.getPhoneNumber())) {
            throw new PhoneNumberDuplicateException();
        }
        if (duplicateCheckService.isNicknameDuplicate(request.getNickname())) {
            throw new NicknameDuplicateException();
        }
    }

    @Before(value = "oauthUser(request, email)", argNames = "request,email")
    public void checkDuplicateOauthUser(OAuthAddUserServiceRequest request, String email) {

        if (duplicateCheckService.isPhoneNumberDuplicate(request.getPhoneNumber())) {
            throw new PhoneNumberDuplicateException();
        }
        if (duplicateCheckService.isNicknameDuplicate(request.getNickname())) {
            throw new NicknameDuplicateException();
        }
    }
}