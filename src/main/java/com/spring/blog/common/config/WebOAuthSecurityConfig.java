package com.spring.blog.common.config;

import com.spring.blog.common.config.authority.CustomAuthorityMapper;
import com.spring.blog.common.config.filter.TokenAuthenticationFilter;
import com.spring.blog.common.config.jwt.TokenProvider;
import com.spring.blog.common.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.spring.blog.common.config.oauth.OAuth2FailureHandler;
import com.spring.blog.common.config.oauth.OAuth2SuccessHandler;
import com.spring.blog.repository.RefreshTokenRepository;
import com.spring.blog.service.CustomUserDetailsService;
import com.spring.blog.service.UserService;
import com.spring.blog.service.oauth.CustomOAuth2UserService;
import com.spring.blog.service.oauth.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebOAuthSecurityConfig {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomOidcUserService customOidcUserService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = setAuthenticationManager(http);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/articles")
                )

                .authenticationManager(authenticationManager)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.*", "/*/icon-*", "/h2-console/**").permitAll()
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                        )
                        .failureHandler(new OAuth2FailureHandler())
                        .successHandler(oAuthSuccessHandler())
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("refresh_token", "JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )

                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
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
    public OAuth2SuccessHandler oAuthSuccessHandler() {
        return new OAuth2SuccessHandler(
                tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }
}