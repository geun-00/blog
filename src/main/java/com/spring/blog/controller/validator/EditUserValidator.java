package com.spring.blog.controller.validator;

import com.spring.blog.domain.User;
import com.spring.blog.dto.EditUserRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class EditUserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return EditUserRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        User user = userService.findByEmail(principalUser.providerUser().getEmail());

        EditUserRequest request = (EditUserRequest) target;

        if (StringUtils.hasText(request.getEmail()) &&
            !user.getEmail().equals(request.getEmail()) &&
            userService.existsByEmail(request.getEmail())) {

            errors.rejectValue("email", "duplicate", new String[]{"이메일"}, null);
        }

        if (StringUtils.hasText(request.getNickname()) &&
            !user.getNickname().equals(request.getNickname()) &&
            userService.existsByNickname(request.getNickname())) {

            errors.rejectValue("nickname", "duplicate", new String[]{"이름"}, null);
        }
    }
}