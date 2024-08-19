package com.spring.blog.common.converters;

import com.spring.blog.model.ProviderUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class DelegatingProviderUserConverter implements Function<ProviderUserRequest, ProviderUser> {

    private final List<Function<ProviderUserRequest, ProviderUser>> converters;

    public DelegatingProviderUserConverter() {

        this.converters = List.of(
                new OAuth2GoogleProviderUserConverter(),
                new OAuth2NaverProviderUserConverter(),
                new OAuth2KakaoProviderUserConverter(),
                new OAuth2KakaoOidcProviderUserConverter()
        );
    }

    @Override
    public ProviderUser apply(ProviderUserRequest providerUserRequest) {

        for (Function<ProviderUserRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.apply(providerUserRequest);

            if (providerUser != null) {
                return providerUser;
            }
        }

        return null;
    }
}
