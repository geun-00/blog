package com.spring.blog.common.converters;

import com.spring.blog.common.config.oauth.logoutHandler.AbstractOAuth2LogoutHandler;
import com.spring.blog.common.config.oauth.logoutHandler.GoogleLogoutHandler;
import com.spring.blog.common.config.oauth.logoutHandler.KakaoLogoutHandler;
import com.spring.blog.common.config.oauth.logoutHandler.NaverLogoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.function.Function;

import static com.spring.blog.common.enums.SocialType.*;

@Component
public class DelegatingOAuth2LogoutHandler {

    private final List<Function<String, AbstractOAuth2LogoutHandler>> logoutHandlers;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.logout.redirect-uri}")
    private String kakaoLogoutUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    public DelegatingOAuth2LogoutHandler(RestTemplate restTemplate) {

        this.logoutHandlers = List.of(
                registrationId -> StringUtils.equals(KAKAO.getSocialName(), registrationId) ?
                        new KakaoLogoutHandler(restTemplate, kakaoClientId, kakaoLogoutUri) : null,

                registrationId -> StringUtils.equals(NAVER.getSocialName(), registrationId) ?
                        new NaverLogoutHandler(restTemplate, naverClientId, naverClientSecret) : null,

                registrationId -> StringUtils.equals(GOOGLE.getSocialName(), registrationId) ?
                        new GoogleLogoutHandler(restTemplate) : null
        );
    }

    public AbstractOAuth2LogoutHandler getLogoutHandler(String registrationId) {

        for (Function<String, AbstractOAuth2LogoutHandler> handler : logoutHandlers) {

            AbstractOAuth2LogoutHandler logoutHandler = handler.apply(registrationId);

            if (logoutHandler != null) {
                return logoutHandler;
            }
        }

        throw new IllegalArgumentException("지원되지 않는 소셜 로그인 유형 " + registrationId);
    }
}