package com.spring.blog.annotation;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.model.PrincipalUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class WithPrincipalUserSecurityContextFactory implements WithSecurityContextFactory<WithPrincipalUser> {

    @Override
    public SecurityContext createSecurityContext(WithPrincipalUser annotation) {

        PrincipalUser principalUser = EasyRandomFactory.createPrincipalUser();

        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().getContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (String role : annotation.roles()) {
            Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                principalUser,
                null,
                grantedAuthorities
        ));

        return context;
    }
}