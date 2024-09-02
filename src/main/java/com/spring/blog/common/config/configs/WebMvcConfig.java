package com.spring.blog.common.config.configs;

import com.spring.blog.common.Interceptors.ExecutionTimeInterceptor;
import com.spring.blog.common.Interceptors.queryCounter.QueryCounter;
import com.spring.blog.common.Interceptors.queryCounter.QueryCounterInterceptor;
import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.common.argumentResolver.UserKeyArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATTERNS = {
            "/images/**", "/css/**", "/js/**", "/style/**", "/*.ico", "/error"
    };

    private final QueryCounter queryCounter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver());
        resolvers.add(new UserKeyArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ExecutionTimeInterceptor())
                .excludePathPatterns(EXCLUDE_PATTERNS);

        registry.addInterceptor(new QueryCounterInterceptor(queryCounter))
                .excludePathPatterns(EXCLUDE_PATTERNS);
    }
}