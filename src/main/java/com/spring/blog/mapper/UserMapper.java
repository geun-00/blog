package com.spring.blog.mapper;

import com.spring.blog.common.annotation.EncodeMapping;
import com.spring.blog.common.enums.SocialType;
import com.spring.blog.controller.dto.request.EditUserRequest;
import com.spring.blog.controller.dto.request.FormAddUserRequest;
import com.spring.blog.controller.dto.request.NewPasswordRequest;
import com.spring.blog.controller.dto.request.OAuthAddUserRequest;
import com.spring.blog.domain.User;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.service.dto.request.EditUserServiceRequest;
import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import com.spring.blog.service.dto.request.NewPasswordServiceRequest;
import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = PasswordEncoderMapper.class
)
public interface UserMapper {

    FormAddUserServiceRequest toServiceRequest(FormAddUserRequest request);
    OAuthAddUserServiceRequest toServiceRequest(OAuthAddUserRequest request);
    EditUserServiceRequest toServiceRequest(EditUserRequest request);
    NewPasswordServiceRequest toServiceRequest(NewPasswordRequest request);

    @Mapping(target = "password", qualifiedBy = EncodeMapping.class)
    User toEntity(FormAddUserServiceRequest request, SocialType registrationId);

    @Mapping(target = "password", qualifiedBy = EncodeMapping.class)
    User toEntity(ProviderUser providerUser, SocialType registrationId);
}