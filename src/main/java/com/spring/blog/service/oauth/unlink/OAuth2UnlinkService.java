package com.spring.blog.service.oauth.unlink;

import com.spring.blog.exception.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UnlinkService {

    private final DelegatingOAuth2UnlinkHandler oAuth2UnlinkHandler;
    private final OAuth2AuthorizedClientService auth2AuthorizedClientService;

    @Async
    public CompletableFuture<Void> unlink(String registrationId, String name) {

        log.info("소셜 연동 해제 요청 시작, Thread : {}", Thread.currentThread());

        try {
            AbstractOAuthUnlinkService unlinkHandler = oAuth2UnlinkHandler.getUnlinkHandler(registrationId);

            OAuth2AuthorizedClient oAuth2AuthorizedClient = auth2AuthorizedClientService.loadAuthorizedClient(registrationId, name);
            String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();

            ResponseEntity<Object> response = unlinkHandler.sendUnlinkRequest(accessToken);

            if (!response.getStatusCode().isError()) {
                log.info("{} 연동 해제 성공 : {}", registrationId, response.getBody());
            }
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(registrationId + " 연동 해제 요청 중 오류", ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException("지원되지 않는 소셜 로그인 유형 : " + registrationId , ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(registrationId + " 연동 해제 중 예상하지 못한 오류 발생", ex);
        }

        return CompletableFuture.completedFuture(null);
    }
}