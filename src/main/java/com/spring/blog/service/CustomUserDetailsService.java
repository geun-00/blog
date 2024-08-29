package com.spring.blog.service;

import com.spring.blog.model.FormUser;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email).map(
                        user -> {
                            ProviderUser provider = FormUser.builder()
                                    .id(UUID.randomUUID().toString())
                                    .username(user.getNickname())
                                    .password(user.getPassword())
                                    .email(user.getEmail())
                                    .authorities(List.of(new SimpleGrantedAuthority("user")))
                                    .build();

                            return new PrincipalUser(provider, null);
                        })
                .orElseThrow(() -> new UsernameNotFoundException("not found :" + email));
    }
}
