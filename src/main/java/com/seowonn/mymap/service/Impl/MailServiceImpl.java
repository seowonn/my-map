package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.EMAIL_SEND_ERROR;

import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.service.MailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
  private final JavaMailSender javaMailSender;

  @Override
  public SimpleMailMessage sendAuthEmail(String emailAddress, String title, String text) {

    SimpleMailMessage message = createEmailForm(emailAddress, title, text);

    try{
      javaMailSender.send(message);

    } catch (RuntimeException e) {

      LOGGER.debug("[sendAuthEmail] : 이메일 전송 과정 에러 발생");
      throw new MyMapSystemException(EMAIL_SEND_ERROR);
    }

    LOGGER.info("[sendAuthEmail] : 이메일 전송 성공");
    return message;
  }

  public SimpleMailMessage createEmailForm(
      String emailAddress, String title, String text) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setSubject(title);
    message.setText(text);

    LOGGER.info("[createEmailForm] : 이메일 생성 완료");

    return message;
  }
}
