package com.spring.blog.service;

import com.spring.blog.common.converters.enums.SocialType;
import com.spring.blog.domain.User;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.dto.EditUserRequest;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
                        .registrationId(SocialType.NONE)
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );
    }

    @Transactional
    public void save(ProviderUser providerUser, SocialType socialType) {
        userRepository.save(
            User.builder()
                    .registrationId(socialType)
                    .email(providerUser.getEmail())
                    .password(passwordEncoder.encode(providerUser.getPassword()))
                    .build()
        );
    }

    @Transactional
    public void updateNickname(String nickname, String email) {
        User user = findByEmail(email);
        user.updateNickname(nickname);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = findByEmail(email);
        userRepository.delete(user);
    }

    @Transactional
    public User editUser(String email, EditUserRequest request) {
        User user = findByEmail(email);
        user.edit(request.getEmail(), request.getNickname());

        return user;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Unexpected User : " + userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Unexpected User : " + email));
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}