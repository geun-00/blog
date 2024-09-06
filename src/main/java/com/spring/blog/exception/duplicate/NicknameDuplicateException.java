package com.spring.blog.exception.duplicate;

public class NicknameDuplicateException extends DuplicateException {

    public NicknameDuplicateException() {
        super("닉네임이 이미 등록되어 있습니다.");
    }
}