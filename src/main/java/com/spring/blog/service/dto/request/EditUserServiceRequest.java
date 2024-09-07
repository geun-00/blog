package com.spring.blog.service.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class EditUserServiceRequest {

    private String nickname;
    private MultipartFile file;
}