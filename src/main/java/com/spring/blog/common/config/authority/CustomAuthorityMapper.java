package com.spring.blog.common.config.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collection;
import java.util.HashSet;

public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());

        boolean isOAuth = false;

        for (GrantedAuthority authority : authorities) {
            if (authority instanceof OAuth2UserAuthority) {
                isOAuth = true;
            }
            mapped.add(mapAuthority(authority.getAuthority()));

        }

        if (isOAuth) {
            mapped.add(new SimpleGrantedAuthority(ROLE_PREFIX + "OAUTH"));
        }
        return mapped;
    }

    private GrantedAuthority mapAuthority(String name) {
        //구글의 스코프 정보 추출
        if (name.lastIndexOf(".") > 0) {
            int index = name.lastIndexOf(".");
            name = name.substring(index + 1);
        }

        name = name.replace("OAUTH2_", ROLE_PREFIX);
        name = name.replace("OIDC_", ROLE_PREFIX);
        name = name.replace("SCOPE_", ROLE_PREFIX);

        if (!name.startsWith(ROLE_PREFIX)) {
            name = ROLE_PREFIX + name;
        }

        return new SimpleGrantedAuthority(name.toUpperCase());
    }
}