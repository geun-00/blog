package com.spring.blog.common.converters;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.common.converters.utils.OAuthAttributesUtils;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.model.social.NaverUser;

import java.util.function.Function;

public class OAuth2NaverProviderUserConverter implements Function<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser apply(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(SocialType.NAVER.getSocialName())) {
            return null;
        }

        return new NaverUser(
                OAuthAttributesUtils.getSubAttributes(providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
