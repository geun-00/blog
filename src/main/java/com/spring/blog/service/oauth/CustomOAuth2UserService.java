package com.spring.blog.service.oauth;

import com.spring.blog.common.converters.ProviderUserRequest;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.UserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public CustomOAuth2UserService(UserService userService, UserRepository userRepository, Function<ProviderUserRequest, ProviderUser> converter) {
        super(userService, userRepository, converter);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);

        ProviderUser providerUser = getProviderUser(providerUserRequest);

        save(providerUser, userRequest);

        return new PrincipalUser(providerUser, null);
    }
}