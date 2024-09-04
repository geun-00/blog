package com.spring.blog.service;

import com.spring.blog.domain.User;
import com.spring.blog.dto.request.VerifyCodeRequest;
import com.spring.blog.exception.SmsException;
import com.spring.blog.exception.VerificationException;
import com.spring.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.secretKey}")
    private String secretKey;

    @Value("${coolsms.from}")
    private String from;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
    }

    public void sendVerificationCode(String to) {

        String verificationCode = generateCode();

        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(to, verificationCode, Duration.ofMinutes(5));

        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[Blog] 인증번호 " + verificationCode + " 를 입력해 주세요.");

        try {
            messageService.send(message);
        } catch (Exception e) {
            log.error("메시지 전송 실패");
            log.error(e.getMessage());
            throw new SmsException("인증번호 전송에 실패했습니다.", e);
        }
    }

    public String verityCode(VerifyCodeRequest request) {
        String requestCode = request.getVerificationCode();

        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        String verificationCode = vo.get(request.getPhoneNumber());
        vo.getOperations().delete(request.getPhoneNumber());

        if (!StringUtils.equals(requestCode, verificationCode)) {
            log.info("이메일 찾기 핸드폰 인증 실패");
            log.info("요청 코드: {}, 인증 코드: {}", requestCode, verificationCode);
            throw new VerificationException("인증 코드가 일치하지 않습니다.");
        }

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(
                () -> new EntityNotFoundException("not found user : " + request.getPhoneNumber()));

        return user.getEmail();
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); //6자리 인증번호 생성
        return String.valueOf(code);
    }
}