package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.SEND_EMAIL;
import static com.seowonn.mymap.type.SuccessMessage.SIGNUP_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.EmailDto;
import com.seowonn.mymap.dto.MemberFormDto;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.service.MemberService;
import com.seowonn.mymap.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/verify/send-email")
  public ApiResponse<?> sendEmail(@RequestBody EmailDto emailDto){
    SimpleMailMessage message = memberService.sendVerificationCode(emailDto);
    return ApiResponse.createSuccessMessage(message, SEND_EMAIL);
  }

  @PostMapping("/user/signup")
  public ApiResponse<?> signUpUser (
      @RequestBody MemberFormDto memberFormDto){
    Member member = memberService.createMember(memberFormDto, Role.USER);
    return ApiResponse.createSuccessMessage(member, SIGNUP_SUCCESS);
  }

  @PostMapping("/admin/signup")
  public ApiResponse<?> signUpAdmin (
      @RequestBody MemberFormDto memberFormDto){
    Member member = memberService.createMember(memberFormDto, Role.ADMIN);
    return ApiResponse.createSuccessMessage(member, SIGNUP_SUCCESS);
  }
}
