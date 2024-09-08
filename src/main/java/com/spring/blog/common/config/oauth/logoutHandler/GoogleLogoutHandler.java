package com.spring.blog.common.config.oauth.logoutHandler;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GoogleLogoutHandler extends AbstractOAuth2LogoutHandler {

    private static final String LOGOUT_PATH = "https://accounts.google.com/o/oauth2/revoke";

    public GoogleLogoutHandler(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getUri(String accessToken) {
        return UriComponentsBuilder.fromHttpUrl(LOGOUT_PATH)
                .queryParam("token", accessToken)
                .toUriString();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}