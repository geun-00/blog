package com.spring.blog.common.config;

import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.common.argumentResolver.UserKeyArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver());
        resolvers.add(new UserKeyArgumentResolver());
    }
}