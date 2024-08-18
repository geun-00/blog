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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("ID로 사용자 조회에 성공한다.")
    @Test
    void findById() {

        // given
        final Long userId = 1L;
        final String email = "test1234";
        final String encodedPassword = "encodedPassword";

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        //Id는 자동으로 생성되어야 하므로 테스트를 위해 수동으로 설정
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        User foundUser = userService.findById(userId);

        // then
        assertThat(foundUser.getId()).isEqualTo(userId);
        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getPassword()).isEqualTo(encodedPassword);

        verify(userRepository, times(1)).findById(userId);
    }

    @DisplayName("존재하지 않는 ID로 사용자 조회 시 예외가 발생한다.")
    @Test
    void failedFindById() {

        // given
        final Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unexpected user");
    }
}