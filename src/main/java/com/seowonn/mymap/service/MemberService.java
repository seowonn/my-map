package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.EmailDto;
import com.seowonn.mymap.dto.MemberFormDto;
import com.seowonn.mymap.dto.SignInForm;
import com.seowonn.mymap.dto.SignInResponse;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.type.Role;
import org.springframework.mail.SimpleMailMessage;

public interface MemberService {

  Member createMember(MemberFormDto memberFormDto, Role role);

  SimpleMailMessage sendVerificationCode(EmailDto emailDto);

  SignInResponse signInMember(SignInForm signInForm);
}
