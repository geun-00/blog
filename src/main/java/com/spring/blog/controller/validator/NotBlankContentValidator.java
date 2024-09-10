package com.spring.blog.controller.validator;

import com.spring.blog.common.annotation.NotBlankContent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class NotBlankContentValidator implements ConstraintValidator<NotBlankContent, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        value = value.trim()
                     .replaceAll("<p>\\s*</p>", "")
                     .replaceAll("<p><br></p>", "")
                     .replaceAll("<p></p>", "");

        return StringUtils.hasText(value);
    }
}
