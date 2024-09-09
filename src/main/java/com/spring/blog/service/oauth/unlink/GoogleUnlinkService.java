package com.spring.blog.service.oauth.unlink;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GoogleUnlinkService extends AbstractOAuthUnlinkService{

    private static final String UNLINK_PATH = "https://accounts.google.com/o/oauth2/revoke";

    public GoogleUnlinkService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getUri(String accessToken) {

        return UriComponentsBuilder.fromHttpUrl(UNLINK_PATH)
                .queryParam("token", accessToken)
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