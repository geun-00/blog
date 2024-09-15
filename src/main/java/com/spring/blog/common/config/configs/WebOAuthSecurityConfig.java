package com.spring.blog.common.config.configs;

import com.spring.blog.common.config.authority.CustomAuthorityMapper;
import com.spring.blog.common.config.oauth.CustomOAuth2SuccessHandler;
import com.spring.blog.common.config.oauth.LoginFailureHandler;
import com.spring.blog.common.config.oauth.logoutHandler.CustomLogoutSuccessHandler;
import com.spring.blog.common.config.oauth.logoutHandler.KakaoProperties;
import com.spring.blog.common.converters.DelegatingOAuth2LogoutHandler;
import com.spring.blog.service.CustomUserDetailsService;
import com.spring.blog.service.UserService;
import com.spring.blog.service.oauth.CustomOAuth2UserService;
import com.spring.blog.service.oauth.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class WebOAuthSecurityConfig {

    private final UserService userService;
    private final KakaoProperties kakaoProperties;
    private final PasswordEncoder passwordEncoder;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2AuthorizedClientService auth2AuthorizedClientService;
    private final DelegatingOAuth2LogoutHandler delegatingOAuth2LogoutHandler;

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

                .authenticationManager(setAuthenticationManager(http))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/style/**", "/js/**", "/images/*", "/webjars/**", "/favicon.*",
                                "/*/icon-*", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

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
                            log.error("{}, {}", request.getMethod(), request.getRequestURI());
                            log.error(authException.getMessage());
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

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(customOAuth2SuccessHandler())
                        .failureHandler(new LoginFailureHandler())
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
        ;

        return http.build();
    }

    private AuthenticationManager setAuthenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailsService);
        provider.setAuthoritiesMapper(customAuthorityMapper());

        builder.authenticationProvider(provider);

        return builder.build();
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(auth2AuthorizedClientService, delegatingOAuth2LogoutHandler, kakaoProperties);
    }

    @Bean
    public CustomOAuth2SuccessHandler customOAuth2SuccessHandler() {
        return new CustomOAuth2SuccessHandler(userService);
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }
}