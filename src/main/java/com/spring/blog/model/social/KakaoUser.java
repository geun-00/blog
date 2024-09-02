package com.spring.blog.model.social;

import com.spring.blog.model.Attributes;
import com.spring.blog.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUser extends OAuth2ProviderUser {

    private Map<String, Object> otherAttr;

    public KakaoUser(Attributes attr, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attr.getSubAttr(), oAuth2User, clientRegistration);
        this.otherAttr = attr.getOtherAttr();
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) otherAttr.get("nickname");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) otherAttr.get("profile_image_url");
    }
}
