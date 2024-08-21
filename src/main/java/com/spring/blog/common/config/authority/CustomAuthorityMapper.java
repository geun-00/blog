package com.spring.blog.common.config.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;

public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());

        for (GrantedAuthority authority : authorities) {

            mapped.add(mapAuthority(authority.getAuthority()));

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