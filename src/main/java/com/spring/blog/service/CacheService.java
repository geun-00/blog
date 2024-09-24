package com.spring.blog.service;

import com.spring.blog.domain.User;
import com.spring.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheService {

    private final UserRepository userRepository;

    @Cacheable(cacheNames = "user", key = "#email")
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("not found user from : " + email));
    }
}