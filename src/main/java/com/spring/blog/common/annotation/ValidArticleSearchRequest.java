package com.spring.blog.common.annotation;

import com.spring.blog.controller.validator.ArticleSearchRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ArticleSearchRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidArticleSearchRequest {

    String message() default "Invalid search request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}