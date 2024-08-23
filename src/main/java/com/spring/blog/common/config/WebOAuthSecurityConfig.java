package com.spring.blog.common.config;

import com.spring.blog.common.config.authority.CustomAuthorityMapper;
import com.spring.blog.common.config.oauth.LoginFailureHandler;
import com.spring.blog.service.CustomUserDetailsService;
import com.spring.blog.service.oauth.CustomOAuth2UserService;
import com.spring.blog.service.oauth.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebOAuthSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login?invalid-session")
                        .maximumSessions(1)
                        .expiredUrl("/login")
                )

                .authenticationManager(setAuthenticationManager(http))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/*", "/webjars/**", "/favicon.*", "/*/icon-*", "/h2-console/**").permitAll()
                        .requestMatchers("/", "/signup", "/login*").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())

                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/loginProc")
                                .usernameParameter("email")
                                .failureHandler(new LoginFailureHandler())
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .failureHandler(new LoginFailureHandler())
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
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

/*
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(
                userService,
                tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
                );
    }
*/

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }
}