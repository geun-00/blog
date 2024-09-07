package com.spring.blog.common.aop;

import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("@annotation(com.spring.blog.common.annotation.DuplicateCheck)")
    private void duplicateCheck(){}

    @Pointcut("duplicateCheck() && args(request)")
    protected void formUser(FormAddUserServiceRequest request){}

    @Pointcut(value = "duplicateCheck() && args(request, email)", argNames = "request,email")
    protected void oauthUser(OAuthAddUserServiceRequest request, String email){}

    @Pointcut("execution(* com.spring.blog.service.UserService.editUser(..))")
    protected void afterEditUser(){}
}