package com.seowonn.mymap.service;

import com.seowonn.mymap.global.dto.EmailDto;
import com.seowonn.mymap.domain.member.dto.member.MemberFormDto;
import com.seowonn.mymap.domain.member.dto.member.MemberResponse;
import com.seowonn.mymap.domain.member.dto.member.SignInForm;
import com.seowonn.mymap.domain.member.dto.member.SignInResponse;
import com.seowonn.mymap.type.Role;
import org.springframework.mail.SimpleMailMessage;

public interface MemberService {

  MemberResponse createMember(MemberFormDto memberFormDto, Role role);

  SimpleMailMessage sendVerificationCode(EmailDto emailDto);
;
  SignInResponse signInMember(SignInForm signInForm);

  void checkVerificationCode(String email, String verificationCode);

  SimpleMailMessage sendNewPassword(EmailDto emailDto);

}
