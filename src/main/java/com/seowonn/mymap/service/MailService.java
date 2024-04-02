package com.seowonn.mymap.service;

import org.springframework.mail.SimpleMailMessage;

public interface MailService {

  SimpleMailMessage sendAuthEmail(String emailAddress, String title, String text);

}
