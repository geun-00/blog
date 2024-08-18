package com.spring.blog.service;

import com.spring.blog.domain.RefreshToken;
import com.spring.blog.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceUnitTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @DisplayName("RefreshToken 조회에 성공한다.")
    @Test
    void findByRefreshToken() {

        // given
        final String token = "validRefreshToken";
        final Long userId = 1L;

        RefreshToken refreshToken = new RefreshToken(userId, token);

        given(refreshTokenRepository.findByRefreshToken(token)).willReturn(Optional.of(refreshToken));

        // when
        RefreshToken foundToken = refreshTokenService.findByRefreshToken(token);

        // then
        assertThat(foundToken).isNotNull();
        assertThat(foundToken.getRefreshToken()).isEqualTo(token);
        assertThat(foundToken.getUserId()).isEqualTo(userId);

        verify(refreshTokenRepository, times(1)).findByRefreshToken(token);
    }

    @DisplayName("존재하지 않는 RefreshToken 조회 시 예외가 발생한다.")
    @Test
    void failedFindByRefreshToken() {

        // given
        final String token = "invalidRefreshToken";

        given(refreshTokenRepository.findByRefreshToken(token)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> refreshTokenService.findByRefreshToken(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unexpected Token");

        verify(refreshTokenRepository, times(1)).findByRefreshToken(token);
    }
}