package com.spring.blog.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record PrincipalUser(ProviderUser providerUser) implements UserDetails, OAuth2User, OidcUser, Authentication {

    @Override
    public String getName() {
        return providerUser.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return providerUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return providerUser.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return providerUser.getPassword();
    }

    @Override
    public Object getDetails() {
        return providerUser;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getPassword() {
        return providerUser.getPassword();
    }

    @Override
    public String getUsername() {
        return providerUser.getUsername();
    }

    @Override
    public Map<String, Object> getClaims() {
        return getAttributes();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
