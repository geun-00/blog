package com.spring.blog.service.sms;

import com.spring.blog.exception.SmsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class ProdMessageService implements MessageService {

    private final DefaultMessageService messageService;

    @Value("${coolsms.from}")
    private String messageFrom;

    @Override
    public void sendMessage(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(messageFrom);
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
}