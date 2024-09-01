package com.spring.blog.common.Interceptors.queryCounter;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Aspect
@Component
@RequiredArgsConstructor
public class QueryCounterAop {

    private final QueryCounter queryCounter;

    @Pointcut("execution(* javax.sql.DataSource.getConnection(..))")
    private void connection(){}

    @Around("connection()")
    public Object getConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        Object connection = joinPoint.proceed();
        return Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                new ConProxyHandler(connection, queryCounter)
        );
    }
}