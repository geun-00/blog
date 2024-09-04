package com.spring.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhoneNumberRequest {

    @NotBlank(message = "필수 입력 값입니다.")
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식에 맞지 않은 입력입니다.")
    private String phoneNumber;

}