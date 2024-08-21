package com.spring.blog.service;

import com.spring.blog.common.converters.enums.SocialType;
import com.spring.blog.domain.User;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(AddUserRequest request) {
        userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );
    }

    @Transactional
    public User save(ProviderUser providerUser, SocialType socialType) {

        return userRepository.save(
            User.builder()
                    .email(providerUser.getEmail())
                    .nickname(providerUser.getUsername())
                    .password(passwordEncoder.encode(providerUser.getPassword()))
                    .registrationId(socialType)
                    .build()
        );
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElse(null);
    }
}