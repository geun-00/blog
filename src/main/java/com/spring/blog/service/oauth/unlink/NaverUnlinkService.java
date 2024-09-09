package com.spring.blog.service.oauth.unlink;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class NaverUnlinkService extends AbstractOAuthUnlinkService {

    private static final String UNLINK_PATH = "https://nid.naver.com/oauth2.0/token";

    private final String clientId;
    private final String clientSecret;

    public NaverUnlinkService(RestTemplate restTemplate, String clientId, String clientSecret) {
        super(restTemplate);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected String getUri(String accessToken) {
        return UriComponentsBuilder.fromHttpUrl(UNLINK_PATH)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("access_token", accessToken)
                .toUriString();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected HttpHeaders getHeaders(String accessToken) {
        return null;
    }
}