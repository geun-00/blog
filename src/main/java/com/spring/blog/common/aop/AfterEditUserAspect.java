package com.spring.blog.common.aop;

import com.spring.blog.domain.User;
import com.spring.blog.model.PrincipalUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AfterEditUserAspect {

    @AfterReturning(pointcut = "Pointcuts.afterEditUser()", returning = "updatedUser")
    public void updateContext(User updatedUser) {

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof PrincipalUser principalUser) {

            log.info("사용자 정보 수정, 세션 업데이트");

            updateContext(principalUser, authentication, updatedUser);
        }
    }

    private void updateContext(PrincipalUser principalUser, Authentication authentication, User user) {

        PrincipalUser updatedPrincipalUser = principalUser.withUpdatedUser(user);

        Authentication newAuth = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            newAuth = new OAuth2AuthenticationToken(
                    updatedPrincipalUser,
                    authentication.getAuthorities(),
                    updatedPrincipalUser.providerUser().getClientRegistration().getRegistrationId()
            );
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            newAuth = new UsernamePasswordAuthenticationToken(
                    updatedPrincipalUser,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
        }

        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(newAuth);
    }
}
