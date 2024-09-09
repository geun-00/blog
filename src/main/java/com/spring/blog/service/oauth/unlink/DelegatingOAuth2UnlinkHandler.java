package com.spring.blog.service.oauth.unlink;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.function.Function;

import static com.spring.blog.common.enums.SocialType.GOOGLE;
import static com.spring.blog.common.enums.SocialType.KAKAO;
import static com.spring.blog.common.enums.SocialType.NAVER;

@Component
public class DelegatingOAuth2UnlinkHandler {

    private final List<Function<String, AbstractOAuthUnlinkService>> unlinkHandlers;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    public DelegatingOAuth2UnlinkHandler(RestTemplate restTemplate) {

        this.unlinkHandlers = List.of(
                registrationId -> StringUtils.equals(KAKAO.getSocialName(), registrationId) ?
                        new KakaoUnlinkService(restTemplate) : null,

                registrationId -> StringUtils.equals(NAVER.getSocialName(), registrationId) ?
                        new NaverUnlinkService(restTemplate, naverClientId, naverClientSecret) : null,

                registrationId -> StringUtils.equals(GOOGLE.getSocialName(), registrationId) ?
                        new GoogleUnlinkService(restTemplate) : null
        );
    }

    public AbstractOAuthUnlinkService getUnlinkHandler(String registrationId) {

        for (Function<String, AbstractOAuthUnlinkService> handler : unlinkHandlers) {

            AbstractOAuthUnlinkService unlinkHandler = handler.apply(registrationId);

            if (unlinkHandler != null) {
                return unlinkHandler;
            }
        }

        throw new IllegalArgumentException("지원되지 않는 소셜 로그인 유형 " + registrationId);
    }
}