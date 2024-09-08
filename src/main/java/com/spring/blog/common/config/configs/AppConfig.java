package com.spring.blog.common.config.configs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.secretKey}")
    private String secretKey;

    private final EntityManager em;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public DefaultMessageService messageService() {
        return NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}