package com.spring.blog.controller.validator;

import com.spring.blog.common.annotation.ConditionalValidation;
import com.spring.blog.controller.dto.request.EditUserRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ConditionalValidator implements ConstraintValidator<ConditionalValidation, EditUserRequest> {

    private static final List<String> ALLOW_FILE_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "image/gif",
            "image/webp"
    );

    @Override
    public boolean isValid(EditUserRequest request, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (StringUtils.hasText(request.nickname()) &&
                (request.nickname().length() < 2 || request.nickname().length() > 20)) {

            context.buildConstraintViolationWithTemplate("별명은 2~20글자 사이만 가능합니다.")
                    .addPropertyNode("nickname")
                    .addConstraintViolation();
            isValid = false;
        }

        MultipartFile file = request.file();
        if (file != null &&
                StringUtils.hasText(file.getOriginalFilename()) &&
                !ALLOW_FILE_TYPES.contains(file.getContentType())
        ) {
            context.buildConstraintViolationWithTemplate("이미지 파일의 형식만 가능합니다.")
                    .addPropertyNode("file")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}