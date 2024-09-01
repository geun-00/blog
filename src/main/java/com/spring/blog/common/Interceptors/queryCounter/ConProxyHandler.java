package com.spring.blog.common.Interceptors.queryCounter;

import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConProxyHandler implements InvocationHandler {

    private final Object connection;
    private final QueryCounter queryCounter;

    public ConProxyHandler(Object connection, QueryCounter queryCounter) {
        this.connection = connection;
        this.queryCounter = queryCounter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        countQuery(method);
        return method.invoke(connection, args);
    }

    private void countQuery(Method method) {
        if (isPstm(method) && isRequest()) {
            queryCounter.increaseCount();
        }
    }

    private boolean isRequest() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    private boolean isPstm(Method method) {
        return method.getName().equals("prepareStatement");
    }
}