package com.spring.blog.exception;

public class ResponseStatusException extends RuntimeException{

    public ResponseStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}