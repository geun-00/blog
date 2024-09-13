package com.spring.blog.exception;

public class ResponseStatusException extends RuntimeException{

    public ResponseStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseStatusException(Throwable cause) {
        super(cause);
    }
}