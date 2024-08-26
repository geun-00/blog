package com.spring.blog.common.converters;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.common.converters.utils.OAuthAttributesUtils;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.model.social.KakaoOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.function.Function;

public class OAuth2KakaoOidcProviderUserConverter implements Function<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser apply(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(SocialType.KAKAO.getSocialName())) {
            return null;
        }
        if (!(providerUserRequest.oAuth2User() instanceof OidcUser)) {
            return null;
        }

        return new KakaoOidcUser(
                OAuthAttributesUtils.getMainAttributes(providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
