package com.spring.blog.common.config.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Blog Project API Document")
                .version("V1")
                .description("개인 블로그 프로젝트 API 명세서");

        SecurityScheme googleOauth2 = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl("https://accounts.google.com/o/oauth2/v2/auth")
                                .tokenUrl("https://www.googleapis.com/oauth2/v4/token")
                                .scopes(new Scopes()
                                        .addString("openid", "")
                                        .addString("profile", "")
                                        .addString("email", "")
                                )
                        )
                );

        SecurityScheme naverOauth2 = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl("https://nid.naver.com/oauth2.0/authorize")
                                .tokenUrl("https://nid.naver.com/oauth2.0/token")
                                .scopes(new Scopes()
                                        .addString("profile", "")
                                        .addString("email", "")
                                )
                        )
                );

        SecurityScheme kakaoOauth2 = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl("https://kauth.kakao.com/oauth/authorize")
                                .tokenUrl("https://kauth.kakao.com/oauth/token")
                                .scopes(new Scopes()
                                        .addString("openid", "")
                                        .addString("profile_nickname", "")
                                        .addString("profile_image", "")
                                        .addString("account_email", "")
                                )
                        )
                );

        SecurityScheme basicAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("googleOauth2", googleOauth2)
                        .addSecuritySchemes("naverOauth2", naverOauth2)
                        .addSecuritySchemes("kakaoOauth2", kakaoOauth2)
                        .addSecuritySchemes("basicAuth", basicAuth)
                )
                .addSecurityItem(new SecurityRequirement().addList("googleOauth2"))
                .addSecurityItem(new SecurityRequirement().addList("naverOauth2"))
                .addSecurityItem(new SecurityRequirement().addList("kakaoOauth2"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .info(info);
    }
}
