package com.seowonn.mymap.domain.email.service;

import static com.seowonn.mymap.global.type.TimeSettings.VERIFICATION_EXPIRE_TIME;
import static com.seowonn.mymap.global.type.ErrorCode.EMAIL_SEND_ERROR;

import com.seowonn.mymap.domain.email.type.AuthenticationContents;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.email.dto.EmailDto;
import com.seowonn.mymap.infra.redis.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    public SimpleMailMessage sendVerificationCode(EmailDto emailDto) {

        String verificationNum = createNumber();

        redisService.setValidationExpireTime(
                emailDto.getEmailAddress(), verificationNum, VERIFICATION_EXPIRE_TIME.getTime());

        return sendAuthEmail(emailDto.getEmailAddress(), verificationNum);
    }

    private String createNumber() {

        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        log.info("[createNumber] : {}, 인증 번호 생성 완료", LocalDateTime.now());

        return String.valueOf(code);
    }


    public SimpleMailMessage sendAuthEmail(String emailAddress, String text) {

        SimpleMailMessage message = createAuthEmailForm(emailAddress, text);

        try {
            javaMailSender.send(message);

        } catch (RuntimeException e) {

            log.debug("[sendAuthEmail] : 이메일 전송 과정 에러 발생");
            throw new MyMapSystemException(EMAIL_SEND_ERROR);
        }

        log.info("[sendAuthEmail] : 수신자 {}, 이메일 전송 성공", emailAddress);
        return message;
    }

    public SimpleMailMessage createAuthEmailForm(String emailAddress, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAddress);
        message.setSubject(AuthenticationContents.EMAIL_AUTH_TITLE.getMessage());
        message.setText(text);

        return message;
    }
}
