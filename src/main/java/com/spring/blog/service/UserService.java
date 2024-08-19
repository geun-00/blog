package com.spring.blog.service;

import com.spring.blog.common.converters.enums.SocialType;
import com.spring.blog.domain.User;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(ProviderUser providerUser, SocialType socialType) {

        return userRepository.save(
            User.builder()
                    .email(providerUser.getEmail())
                    .nickname(providerUser.getUsername())
                    .registrationId(socialType)
                    .build()
        );
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}