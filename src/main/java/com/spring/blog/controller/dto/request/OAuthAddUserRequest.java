package com.spring.blog.controller.dto.request;

import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class OAuthAddUserRequest {

    @NotBlank
    @Pattern(regexp = "^010\\d{8}$",
            message = "올바른 형식의 전화번호여야 합니다.")
    private String phoneNumber;

    @NotBlank
    @Length(min = 2, max = 20)
    private String nickname;


    public OAuthAddUserServiceRequest toServiceRequest() {
        return OAuthAddUserServiceRequest.builder()
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .build();
    }
}