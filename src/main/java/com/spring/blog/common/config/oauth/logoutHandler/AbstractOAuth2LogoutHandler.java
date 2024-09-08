package com.spring.blog.common.config.oauth.logoutHandler;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOAuth2LogoutHandler {

    protected RestTemplate restTemplate;

    public AbstractOAuth2LogoutHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Object> sendLogoutRequest(String accessToken) {

        String uri = getUri(accessToken);
        HttpMethod httpMethod = getHttpMethod();

        return restTemplate.exchange(uri, httpMethod, null, Object.class);
    }

    protected abstract String getUri(String accessToken);
    protected abstract HttpMethod getHttpMethod();
}