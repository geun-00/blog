package com.spring.blog.common.Interceptors.queryCounter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class QueryCounterInterceptor implements HandlerInterceptor {

    private final QueryCounter queryCounter;

    public QueryCounterInterceptor(QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int count = queryCounter.getCount();

        log.info("Query Count : {}", count);
    }
}
