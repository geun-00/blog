package com.spring.blog.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneNumberRequest(

        @NotBlank(message = "필수 입력 값입니다.")
        @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식에 맞지 않은 입력입니다.")
        String phoneNumber
) {
}