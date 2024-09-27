package com.spring.blog.service.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"local", "dev", "test"})
public class DevMessageService implements MessageService {

    @Override
    public void sendMessage(String to, String verificationCode) {

        log.info("{} 으로 인증 번호를 보냅니다.", to);
        log.info("[Blog] 인증번호 " + verificationCode + " 를 입력해 주세요.");
    }
}