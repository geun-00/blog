package com.spring.blog.annotation;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.model.PrincipalUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithPrincipalUserSecurityContextFactory implements WithSecurityContextFactory<WithPrincipalUser> {

    @Override
    public SecurityContext createSecurityContext(WithPrincipalUser annotation) {

        PrincipalUser principalUser = EasyRandomFactory.createPrincipalUser();

        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().getContext();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                principalUser,
                null,
                principalUser.getAuthorities()
        ));

        return context;
    }
}