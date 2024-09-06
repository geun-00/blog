package com.spring.blog.controller.dto.request;

import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class FormAddUserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$",
            message = "비밀번호 요구사항과 맞지 않습니다.")
    private String password;

    @NotBlank
    @Length(min = 2, max = 20)
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^010\\d{8}$",
            message = "올바른 형식의 전화번호여야 합니다.")
    private String phoneNumber;


    public FormAddUserServiceRequest toServiceRequest() {
        return FormAddUserServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .build();
    }
}