package com.spring.blog.service;

import com.spring.blog.domain.User;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(AddUserRequest request) {
        return userRepository.save(
                        User.builder()
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .build())
                .getId();
    }

}
