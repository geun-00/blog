package com.spring.blog.common.config.configs;

import com.spring.blog.common.Interceptors.ExecutionTimeInterceptor;
import com.spring.blog.common.Interceptors.FileCleanUpInterceptor;
import com.spring.blog.common.Interceptors.queryCounter.QueryCounter;
import com.spring.blog.common.Interceptors.queryCounter.QueryCounterInterceptor;
import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.common.argumentResolver.UserKeyArgumentResolver;
import com.spring.blog.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATTERNS = {
            "/images/**", "/style/**", "/js/**", "/style/**", "/*.ico", "/error"
    };

    private final FileService fileService;
    private final QueryCounter queryCounter;
    private final RedisTemplate<String, Object> redisTemplate;

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

        registry.addInterceptor(new FileCleanUpInterceptor(fileService, redisTemplate))
                .addPathPatterns("/api/articles/**")
                .excludePathPatterns("/api/articles/like/**", "/api/articles/search")
                .excludePathPatterns(EXCLUDE_PATTERNS);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/500").setViewName("error/500");
        registry.addViewController("/findEmail").setViewName("phone-number-request");
        registry.addViewController("/findPassword").setViewName("email-request");

        registry.addRedirectViewController("/", "/guest");
    }
}