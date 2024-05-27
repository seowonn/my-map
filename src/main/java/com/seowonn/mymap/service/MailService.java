package com.seowonn.mymap.service;

import static com.seowonn.mymap.type.AuthenticationContents.EMAIL_AUTH_TITLE;
import static com.seowonn.mymap.global.type.ErrorCode.EMAIL_SEND_ERROR;

import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender javaMailSender;


  public SimpleMailMessage sendAuthEmail(String emailAddress, String text) {

    SimpleMailMessage message = createAuthEmailForm(emailAddress, text);

    try{
      javaMailSender.send(message);

    } catch (RuntimeException e) {

      log.debug("[sendAuthEmail] : 이메일 전송 과정 에러 발생");
      throw new MyMapSystemException(EMAIL_SEND_ERROR);
    }

    log.info("[sendAuthEmail] : 이메일 전송 성공");
    return message;
  }

  public SimpleMailMessage createAuthEmailForm(String emailAddress, String text) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setSubject(EMAIL_AUTH_TITLE.getMessage());
    message.setText(text);

    log.info("[createEmailForm] : 이메일 생성 완료");

    return message;
  }
}
