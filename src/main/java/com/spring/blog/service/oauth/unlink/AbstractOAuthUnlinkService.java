package com.spring.blog.service.oauth.unlink;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOAuthUnlinkService {

    protected RestTemplate restTemplate;

    public AbstractOAuthUnlinkService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Object> sendUnlinkRequest(String accessToken) {

        String uri = getUri(accessToken);
        HttpMethod httpMethod = getHttpMethod();
        HttpHeaders headers = getHeaders(accessToken);

        HttpEntity<Object> entity = (headers != null) ? new HttpEntity<>(headers) : null;

        return restTemplate.exchange(uri, httpMethod, entity, Object.class);
    }

    protected abstract String getUri(String accessToken);
    protected abstract HttpMethod getHttpMethod();
    protected abstract HttpHeaders getHeaders(String accessToken);
}