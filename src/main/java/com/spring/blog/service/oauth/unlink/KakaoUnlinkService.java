package com.spring.blog.service.oauth.unlink;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class KakaoUnlinkService extends AbstractOAuthUnlinkService {

    private static final String UNLINK_PATH = "https://kapi.kakao.com/v1/user/unlink";

    public KakaoUnlinkService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getUri(String accessToken) {
        return UNLINK_PATH;
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected HttpHeaders getHeaders(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        return headers;
    }
}