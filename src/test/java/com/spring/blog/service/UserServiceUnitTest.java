package com.spring.blog.service;

import com.spring.blog.domain.User;
import com.spring.blog.dto.AddUserRequest;
import com.spring.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @DisplayName("사용자 저장에 성공한다.")
    @Test
    void saveUser() {

        // given
        final String email = "test1234";
        final String password = "password";
        final String encodedPassword = "encodedPassword";

        AddUserRequest request = new AddUserRequest(email, password);
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        Long savedUserId = userService.save(request);

        // then
        assertThat(savedUserId).isEqualTo(user.getId());
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }
}