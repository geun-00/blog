package com.spring.blog.exception.duplicate;

public class EmailDuplicateException extends DuplicateException {

    public EmailDuplicateException() {
        super("이메일이 이미 등록되어 있습니다.");
    }
}