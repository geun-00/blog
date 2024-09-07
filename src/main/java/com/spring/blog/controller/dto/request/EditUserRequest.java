package com.spring.blog.controller.dto.request;

import com.spring.blog.common.annotation.ConditionalValidation;
import com.spring.blog.service.dto.request.EditUserServiceRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@ConditionalValidation
public class EditUserRequest {

    private String nickname;
    private MultipartFile file;

    public EditUserServiceRequest toServiceRequest() {
        return EditUserServiceRequest.builder()
                .nickname(nickname)
                .file(file)
                .build();
    }
}