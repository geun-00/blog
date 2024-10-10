package com.spring.blog.service.integration;

import com.spring.blog.IntegrationTestSupport;
import com.spring.blog.domain.User;
import com.spring.blog.exception.duplicate.DuplicateException;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.UserService;
import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserService 통합 테스트")
@Transactional
public class UserServiceTest extends IntegrationTestSupport {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @DisplayName("save() 테스트, 정상 흐름")
    @Test
    void save() {
        // given
        String email = "email@test.com";
        String nickname = "testNickname";
        String password = "password12@";
        String phoneNumber = "01011223344";

        FormAddUserServiceRequest request = new FormAddUserServiceRequest(email, password, nickname, phoneNumber);

        // when
        String result = userService.save(request);

        // then
        assertThat(result).isEqualTo(nickname);

        Optional<User> user = userRepository.findByEmail(email);
        assertThat(user).isPresent();

        User savedUser = user.get();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getNickname()).isEqualTo(nickname);
        assertThat(savedUser.getPassword()).isNotEqualTo(password); //비밀번호 암호화
        assertThat(savedUser.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @DisplayName("save() 테스트, 예외 흐름")
    @ParameterizedTest(name = "{3}")
    @CsvSource({
            "user@test.com, testNickname, 01011223344, 이메일이 이미 등록되어 있습니다.",
            "test@email.com, nickname0, 01011223344, 닉네임이 이미 등록되어 있습니다.",
            "test@email.com, testNickname, 01012341234, 전화번호가 이미 등록되어 있습니다.",
    })
    void saveWithEx(String email, String nickname, String phoneNumber, String errorMessage) {
        // given
        String password = "password12@";
        FormAddUserServiceRequest request = new FormAddUserServiceRequest(email, password, nickname, phoneNumber);

        // when
        // then
        assertThatThrownBy(() -> userService.save(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(errorMessage);
    }

    @DisplayName("updateOAuthUser() 테스트, 정상 흐름")
    @Test
    void updateOAuthUser() {
        // given
        String email = "user@test.com";

        String updateNickname = "updateNickname";
        String updatePhoneNumber = "01013572468";

        OAuthAddUserServiceRequest request = new OAuthAddUserServiceRequest(updatePhoneNumber, updateNickname);

        // when
        String result = userService.updateOAuthUser(request, email);

        // then
        assertThat(result).isEqualTo(updateNickname);

        User user = userRepository.findByEmail(email).get();
        assertThat(user.getPhoneNumber()).isEqualTo(updatePhoneNumber);
        assertThat(user.getNickname()).isEqualTo(updateNickname);
    }

    @DisplayName("updateOAuthUser() 테스트, 예외 흐름")
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "nickname0, 01011223344, 닉네임이 이미 등록되어 있습니다.",
            "updateNickname, 01012341234, 전화번호가 이미 등록되어 있습니다.",
    })
    void updateOAuthUserWithEx(String updateNickname, String updatePhoneNumber, String errorMessage) {
        // given
        String email = "user@test.com";
        OAuthAddUserServiceRequest request = new OAuthAddUserServiceRequest(updatePhoneNumber, updateNickname);

        // when
        // then
        assertThatThrownBy(() -> userService.updateOAuthUser(request, email))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(errorMessage);
    }
}
