package com.spring.blog.controller.validator;

import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AddUserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return AddUserRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AddUserRequest request = (AddUserRequest) target;

        if (userService.existsByEmail(request.getEmail())) {
            errors.rejectValue("email","Duplicate","이미 사용 중인 이메일입니다.");
        }
        if (userService.existsByNickname(request.getNickname())) {
            errors.rejectValue("nickname","Duplicate","이미 사용 중인 이름입니다.");
        }
    }
}
