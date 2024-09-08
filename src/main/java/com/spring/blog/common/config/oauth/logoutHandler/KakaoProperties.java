package com.spring.blog.common.config.oauth.logoutHandler;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Component
@ConfigurationProperties(prefix = "kakao.logout")
public class KakaoProperties {

    @NotBlank
    private String uri;

    @NotBlank
    private String clientId;

    @NotBlank
    private String redirectUri;

    public String getRedirectUri() {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("client_id", clientId)
                .queryParam("logout_redirect_uri", redirectUri)
                .toUriString();
    }
}