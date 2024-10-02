package com.spring.blog.controller.unit;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.controller.ApiControllerUnitTestSupport;
import com.spring.blog.controller.dto.request.EmailRequest;
import com.spring.blog.controller.dto.request.EmailVerifyCodeRequest;
import com.spring.blog.controller.dto.request.PhoneNumberRequest;
import com.spring.blog.controller.dto.request.SmsVerifyCodeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("사용자 인증 API 단위 테스트")
public class UserVerifyApiControllerUnitTest extends ApiControllerUnitTestSupport {

    @DisplayName("POST /api/verify/phoneNumber 테스트, 정상 흐름")
    @Test
    void verifyPhoneNumber() throws Exception {
        // given
        String phoneNumber = "01012341234";
        PhoneNumberRequest request = new PhoneNumberRequest(phoneNumber);

        CompletableFuture<Boolean> future = CompletableFuture.completedFuture(true);
        given(verificationService.sendVerificationCodeBySms(phoneNumber)).willReturn(future);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/verify/phoneNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("인증번호 전송"));

        // then
        verify(verificationService, times(1)).sendVerificationCodeBySms(phoneNumber);
    }

    @DisplayName("POST /api/verify/phoneNumber 테스트, 예외 흐름(유효성 체크)")
    @ParameterizedTest(name = "{1}")
    @CsvSource({
            "'', null 체크",
            "021234567, 유효성 체크",
    })
    void verifyPhoneNumberWithValidEx(String phoneNumber, String message) throws Exception {

        // given
        PhoneNumberRequest request = new PhoneNumberRequest(phoneNumber);

        // when
        MvcResult result = mockMvc.perform(post("/api/verify/phoneNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        //then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();

        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("POST /api/verify/code/sms 테스트, 정상 흐름")
    @Test
    void verifyCodeBySms() throws Exception {
        // given
        String phoneNumber = "01012341234";
        String verificationCode = "012345";
        SmsVerifyCodeRequest request = new SmsVerifyCodeRequest(phoneNumber, verificationCode);

        String foundEmail = "user@test.com";
        given(verificationService.verifyCodeBySms(anyString(), anyString())).willReturn(foundEmail);

        // when
        mockMvc.perform(post("/api/verify/code/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value(foundEmail));

        // then
        verify(verificationService, times(1)).verifyCodeBySms(anyString(), anyString());
    }

    @DisplayName("POST /api/verify/code/sms 테스트, 예외 흐름(유효성 체크)")
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "'', 012345, phoneNumber null 체크",
            "021234567, 012345, phoneNumber 유효성 체크",
            "01012341234, '', verificationCode null 체크",
            "01012341234, 012, verificationCode 유효성 체크",
    })
    void verifyCodeBySmsWithValidEx(String phoneNumber, String verificationCode, String message) throws Exception {

        // given
        SmsVerifyCodeRequest request = new SmsVerifyCodeRequest(phoneNumber, verificationCode);

        // when
        MvcResult result = mockMvc.perform(post("/api/verify/code/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        //then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();

        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("POST /api/verify/email 테스트, 정상 흐름")
    @Test
    void verifyEmail() throws Exception {
        // given
        String email = "user@test.com";
        EmailRequest request = new EmailRequest(email);

        CompletableFuture<Boolean> future = CompletableFuture.completedFuture(true);
        given(verificationService.sendVerificationCodeByEmail(email)).willReturn(future);

        // when

        MvcResult mvcResult = mockMvc.perform(post("/api/verify/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("인증번호 전송"));

        // then
        verify(verificationService, times(1)).sendVerificationCodeByEmail(email);
    }

    @DisplayName("POST /api/verify/email 테스트, 예외 흐름(유효성 체크)")
    @Test
    void verifyEmailWithValidEx() throws Exception {

        // given
        String email = "wrongEmail";
        EmailRequest request = new EmailRequest(email);

        // when
        MvcResult result = mockMvc.perform(post("/api/verify/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        //then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();

        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("POST /api/verify/code/email 테스트, 정상 흐름")
    @Test
    void verifyCodeByEmail() throws Exception {
        // given
        String verificationCode = "012345";
        String email = "user@test.com";
        EmailVerifyCodeRequest request = new EmailVerifyCodeRequest(email, verificationCode);

        given(verificationService.verifyCodeByEmail(anyString(), anyString())).willReturn(SocialType.NONE);

        // when
        mockMvc.perform(post("/api/verify/code/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("none"));

        // then
        verify(verificationService, times(1)).verifyCodeByEmail(anyString(), anyString());
    }

    @DisplayName("POST /api/verify/code/email 테스트, 예외 흐름(유효성 체크)")
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "'', 012345, email null 체크",
            "wrongEmail, 012345, email 유효성 체크",
            "user@test.com, '', verificationCode null 체크",
            "user@test.com , 012, verificationCode 유효성 체크",
    })
    void verifyCodeByEmailWithValidEx(String email, String verificationCode, String message) throws Exception {

        // given
        EmailVerifyCodeRequest request = new EmailVerifyCodeRequest(email, verificationCode);

        // when
        MvcResult result = mockMvc.perform(post("/api/verify/code/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        //then
        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();

        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }
}
