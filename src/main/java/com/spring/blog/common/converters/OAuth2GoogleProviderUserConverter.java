package com.spring.blog.common.converters;

import com.spring.blog.common.converters.enums.SocialType;
import com.spring.blog.common.converters.utils.OAuthAttributesUtils;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.model.social.GoogleUser;

import java.util.function.Function;

public class OAuth2GoogleProviderUserConverter implements Function<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser apply(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(SocialType.GOOGLE.getSocialName())) {
            return null;
        }

        return new GoogleUser(
                OAuthAttributesUtils.getMainAttributes(providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
