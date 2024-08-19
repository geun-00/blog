package com.spring.blog.service.oauth;

import com.spring.blog.common.converters.ProviderUserRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    public CustomOidcUserService(UserService userService, UserRepository userRepository, Function<ProviderUserRequest, ProviderUser> converter) {
        super(userService, userRepository, converter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = ClientRegistration
                .withClientRegistration(userRequest.getClientRegistration()) //기존에 정의한 설정은 그대로
                .userNameAttributeName("sub") //필요한 부분은 재정의
                .build();

        //Oidc 인증의 UserInfo 엔드 포인트 요청을 위한 객체 재정의
        OidcUserRequest oidcUserRequest = new OidcUserRequest(
                clientRegistration,
                userRequest.getAccessToken(),
                userRequest.getIdToken(),
                userRequest.getAdditionalParameters()
        );

        OAuth2UserService<OidcUserRequest, OidcUser> delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(oidcUserRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);

        ProviderUser providerUser = getProviderUser(providerUserRequest);

        save(providerUser, userRequest);

        return new PrincipalUser(providerUser);
    }
}