package com.spring.blog.mapper;

import com.spring.blog.common.annotation.EncodeMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    private final PasswordEncoder passwordEncoder;

    @EncodeMapping
    public String encode(String value) {
        return passwordEncoder.encode(value);
    }
}