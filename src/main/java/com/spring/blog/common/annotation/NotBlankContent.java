package com.spring.blog.common.annotation;

import com.spring.blog.controller.validator.NotBlankContentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankContentValidator.class)
public @interface NotBlankContent {

    String message() default "내용이 비어있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
