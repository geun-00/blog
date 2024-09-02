package com.spring.blog.model.social;

import com.spring.blog.model.Attributes;
import com.spring.blog.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuth2ProviderUser {

    public GoogleUser(Attributes mainAttr, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(mainAttr.getMainAttr(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("name");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) getAttributes().get("picture");
    }
}
