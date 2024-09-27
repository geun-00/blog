package com.spring.blog.service.sms;

public interface MessageService {
    void sendMessage(String to, String verificationCode);
}