package com.spring.blog;

import com.spring.blog.common.config.oauth.LoginFailureHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .expiredUrl("/login")
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/style/**", "/js/**", "/images/*", "/webjars/**", "/favicon.*",
                                "/*/icon-*", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()

                        .requestMatchers("/", "/login*", "/userPage*").permitAll() //사용자 관련 뷰 설정
                        .requestMatchers("/api/formUser").permitAll() //사용자 관련 API 설정

                        .requestMatchers("/guest", "/articles/**").permitAll() //게시글 관련 뷰 설정
                        .requestMatchers("/api/articles/search", "/api/articles/page",
                                "/api/articles/{articleId}/liked").permitAll() //게시글 관련 API 설정

                        .requestMatchers("/api/verify/**").permitAll() //이메일, 비밀번호 찾기 API

                        .requestMatchers("/error/**").permitAll() //에러 화면

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/error/403");
                        })
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/articles")
                        .failureHandler(new LoginFailureHandler())
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
        ;

        return http.build();
    }
}
