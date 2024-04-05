package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.EmailDto;
import com.seowonn.mymap.dto.member.MemberFormDto;
import com.seowonn.mymap.dto.member.SignInForm;
import com.seowonn.mymap.dto.member.SignInResponse;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.type.Role;
import org.springframework.mail.SimpleMailMessage;

public interface MemberService {

  Member createMember(MemberFormDto memberFormDto, Role role);

  SimpleMailMessage sendVerificationCode(EmailDto emailDto);
;
  SignInResponse signInMember(SignInForm signInForm);

  void checkVerificationCode(String email, String verificationCode);
}
