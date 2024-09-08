package com.spring.blog.common.config.oauth.logoutHandler;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class NaverLogoutHandler extends AbstractOAuth2LogoutHandler {

    private static final String LOGOUT_PATH = "https://nid.naver.com/oauth2.0/token";

    private final String clientId;
    private final String clientSecret;

    public NaverLogoutHandler(RestTemplate restTemplate, String clientId, String clientSecret) {
        super(restTemplate);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected String getUri(String accessToken) {
        return UriComponentsBuilder.fromHttpUrl(LOGOUT_PATH)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER")
                .toUriString();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}