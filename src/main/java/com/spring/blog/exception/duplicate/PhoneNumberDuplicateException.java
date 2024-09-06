package com.spring.blog.exception.duplicate;

public class PhoneNumberDuplicateException extends DuplicateException {

    public PhoneNumberDuplicateException() {
        super("전화번호가 이미 등록되어 있습니다.");
    }
}