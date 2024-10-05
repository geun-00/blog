package com.spring.blog.exception;

public class CoordinateConvertException extends RuntimeException {

    public CoordinateConvertException(String message) {
        super(message);
    }

    public CoordinateConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
