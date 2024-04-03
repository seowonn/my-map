package com.seowonn.mymap.service;

import org.springframework.mail.SimpleMailMessage;

public interface MailService {

  SimpleMailMessage sendAuthEmail(String emailAddress, String text);

  SimpleMailMessage createAuthEmailForm(String emailAddress, String text);

}
