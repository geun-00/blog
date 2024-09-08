package com.spring.blog.common.config.oauth.logoutHandler;

import com.spring.blog.common.converters.DelegatingOAuth2LogoutHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final OAuth2AuthorizedClientService auth2AuthorizedClientService;
    private final DelegatingOAuth2LogoutHandler delegatingOAuth2LogoutHandler;
    private final KakaoProperties kakaoProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/login";

        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {

            String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            String name = oAuth2AuthenticationToken.getPrincipal().getName();
            OAuth2AuthorizedClient oAuth2AuthorizedClient = auth2AuthorizedClientService.loadAuthorizedClient(registrationId, name);

            String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();


            try {
                AbstractOAuth2LogoutHandler logoutHandler = delegatingOAuth2LogoutHandler.getLogoutHandler(registrationId);

                if (logoutHandler instanceof KakaoLogoutHandler) {
                    redirectUrl = kakaoProperties.getRedirectUri();
                }

                ResponseEntity<Object> responseEntity = logoutHandler.sendLogoutRequest(accessToken);
                if (!responseEntity.getStatusCode().isError()) {
                    log.info("{} 로그아웃 성공 : {}", registrationId, responseEntity.getBody());
                }
            } catch (IllegalArgumentException ex) {
                log.error("지원되지 않는 소셜 로그인 유형: {}", registrationId);
                redirectUrl = "error/500";
            } catch (HttpClientErrorException ex) {
                log.error("{} 로그아웃 요청 에러 발생: {}", registrationId, ex.getMessage());
                redirectUrl = "error/500";
            } catch (Exception ex) {
                log.error("{} 로그아웃 요청 중 예기치 못한 에러 발생: {}", registrationId, ex.getMessage());
                redirectUrl = "error/500";
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
