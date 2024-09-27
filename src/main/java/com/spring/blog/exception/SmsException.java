package com.spring.blog.exception;

public class SmsException extends RuntimeException{

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsException(String message) {
        super(message);
    }
}
