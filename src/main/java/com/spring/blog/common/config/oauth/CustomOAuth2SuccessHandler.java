package com.spring.blog.common.config.oauth;

import com.spring.blog.domain.User;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    private static final String REDIRECT_PATH_NICKNAME_REQUIRED = "/signup";
    private static final String REDIRECT_PATH_AUTH_SUCCESS = "/articles";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        RedirectStrategy redirectStrategy = getRedirectStrategy();

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        String email = principalUser.providerUser().getEmail();

        User user = userService.findByEmail(email);

        //별명 설정이 아직 안 된 경우, 별명 설정하러 이동
        if (!StringUtils.hasText(user.getNickname())) {
            redirectStrategy.sendRedirect(request, response, REDIRECT_PATH_NICKNAME_REQUIRED);
        }
        //별명 설정까지 다 된 경우, 세션에서 사용자 정보를 가져올 수 있도록 인증 정보 재정의 후 메인 페이지 이동
        else {
            principalUser = new PrincipalUser(principalUser.providerUser(), user);

            OAuth2AuthenticationToken oAuth2AuthenticationToken =
                    new OAuth2AuthenticationToken(
                            principalUser,
                            authentication.getAuthorities(),
                            principalUser.providerUser().getClientRegistration().getRegistrationId()
                    );

            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(oAuth2AuthenticationToken);

            redirectStrategy.sendRedirect(request, response, REDIRECT_PATH_AUTH_SUCCESS);
        }
    }
}