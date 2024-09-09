package com.spring.blog.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TransactionalLoggingAspect {

    @Around("Pointcuts.transactionalMethods()")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("트랜잭션 시작 : {}", methodName);

        Object result;

        try {
            result = joinPoint.proceed();
            log.info("트랜잭션 커밋 : {}", methodName);
        } catch (Exception e) {
            log.error("오류 발생, 트랜잭션 롤백 : {}", methodName);
            throw e;
        }

        return result;
    }
}