package com.spring.blog.common.config.oauth.logoutHandler;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class KakaoLogoutHandler extends AbstractOAuth2LogoutHandler {

    private static final String LOGOUT_PATH = "https://kauth.kakao.com/oauth/logout";

    private final String clientId;
    private final String logoutUri;

    public KakaoLogoutHandler(RestTemplate restTemplate, String kakaoClientId, String logoutUri) {
        super(restTemplate);
        this.clientId = kakaoClientId;
        this.logoutUri = logoutUri;
    }

    @Override
    protected String getUri(String accessToken) {

        return UriComponentsBuilder.fromHttpUrl(LOGOUT_PATH)
                .queryParam("client_id", clientId)
                .queryParam("logout_redirect_uri", logoutUri)
                .toUriString();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}