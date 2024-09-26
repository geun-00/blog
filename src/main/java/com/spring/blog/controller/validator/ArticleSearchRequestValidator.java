package com.spring.blog.controller.validator;

import com.spring.blog.common.annotation.ValidArticleSearchRequest;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class ArticleSearchRequestValidator implements ConstraintValidator<ValidArticleSearchRequest, ArticleSearchRequest> {

    @Override
    public boolean isValid(ArticleSearchRequest request, ConstraintValidatorContext context) {

        if (request == null) {
            return false;
        }

        switch (request.searchType()) {
            case TITLE -> {
                if (!StringUtils.hasText(request.title())) {
                    addConstraint(context, "검색하는 제목이 비어있습니다.", "title");
                    return false;
                }
            }
            case AUTHOR -> {
                if (!StringUtils.hasText(request.author())) {
                    addConstraint(context, "검색하는 작성자가 비어있습니다.", "author");
                    return false;
                }
            }
            case CONTENT -> {
                if (!StringUtils.hasText(request.content())) {
                    addConstraint(context, "검색하는 내용이 비어있습니다.", "content");
                    return false;
                }
            }
            case TITLE_CONTENT -> {
                if (request.titleContent() == null ||
                    !StringUtils.hasText(request.titleContent().title()) ||
                    !StringUtils.hasText(request.titleContent().content())
                )
                 {
                    addConstraint(context, "검색하는 제목 또는 내용이 비어있습니다.", "titleContent");
                    return false;
                }
            }
            case PERIOD -> {
                if (request.period() == null ||
                    request.period().startDate() == null ||
                    request.period().endDate() == null ||
                    request.period().startDate().isAfter(request.period().endDate())
                ) {
                    addConstraint(context, "유효한 기간이 아닙니다.", "period");
                    return false;
                }
            }
            default -> {
                context.buildConstraintViolationWithTemplate("알 수 없는 검색 유형");
                return false;
            }
        }

        return true;
    }

    private void addConstraint(ConstraintValidatorContext context, String messages, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messages)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
