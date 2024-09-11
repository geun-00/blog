package com.spring.blog.controller.validator;

import com.spring.blog.common.annotation.ValidArticleSearchRequest;
import com.spring.blog.dto.request.ArticleSearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class ArticleSearchRequestValidator implements ConstraintValidator<ValidArticleSearchRequest, ArticleSearchRequest> {

    @Override
    public boolean isValid(ArticleSearchRequest request, ConstraintValidatorContext context) {

        if (request == null) {
            return false;
        }

        switch (request.getSearchType()) {
            case TITLE -> {
                if (!StringUtils.hasText(request.getTitle())) {
                    addConstraint(context, "검색하는 제목이 비어있습니다.", "title");
                    return false;
                }
            }
            case AUTHOR -> {
                if (!StringUtils.hasText(request.getAuthor())) {
                    addConstraint(context, "검색하는 작성자가 비어있습니다.", "author");
                    return false;
                }
            }
            case CONTENT -> {
                if (!StringUtils.hasText(request.getContent())) {
                    addConstraint(context, "검색하는 내용이 비어있습니다.", "content");
                    return false;
                }
            }
            case TITLE_CONTENT -> {
                if (request.getTitleContent() == null ||
                   (!StringUtils.hasText(request.getTitleContent().getTitle()) &&
                    !StringUtils.hasText(request.getTitleContent().getContent())
                   )
                ) {
                    addConstraint(context, "검색하는 제목 또는 내용이 비어있습니다.", "titleContent");
                    return false;
                }
            }
            case PERIOD -> {
                if (request.getPeriod() == null ||
                    request.getPeriod().getStartDate() == null ||
                    request.getPeriod().getEndDate() == null ||
                    request.getPeriod().getStartDate().isAfter(request.getPeriod().getEndDate())
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
