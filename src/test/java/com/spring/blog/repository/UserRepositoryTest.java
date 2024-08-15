package com.spring.blog.repository;

import com.spring.blog.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일로 사용자 조회에 성공한다.")
    @Test
    void findByEmail() {

        // given
        final String email = "test@email.com";
        User user = User.builder()
                .email(email)
                .password("encodedPassword")
                .build();
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByEmail(email);

        // then
        assertThat(foundUser).isPresent()
                .hasValueSatisfying(u->{
                    assertThat(u.getEmail()).isEqualTo(email);
                    assertThat(u.getPassword()).isEqualTo(user.getPassword());
                });
    }
}