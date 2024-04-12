package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.SEND_EMAIL;
import static com.seowonn.mymap.type.SuccessMessage.SIGNUP_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.SIGN_IN_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.EmailDto;
import com.seowonn.mymap.dto.member.MemberFormDto;
import com.seowonn.mymap.dto.member.SignInForm;
import com.seowonn.mymap.dto.member.SignInResponse;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.service.MemberService;
import com.seowonn.mymap.type.Role;
import jakarta.validation.Valid;
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
  public ApiResponse<?> sendEmail(@Valid @RequestBody EmailDto emailDto){
    SimpleMailMessage message = memberService.sendVerificationCode(emailDto);
    return ApiResponse.createSuccessMessage(message, SEND_EMAIL);
  }

  @PostMapping("/standard/signup")
  public ApiResponse<?> signUpStandardUser(
      @Valid @RequestBody MemberFormDto memberFormDto){
    Member member = memberService.createMember(memberFormDto, Role.USER);
    return ApiResponse.createSuccessMessage(member, SIGNUP_SUCCESS);
  }

  @PostMapping("/admin/signup")
  public ApiResponse<?> signUpAdmin (
      @Valid @RequestBody MemberFormDto memberFormDto){
    Member member = memberService.createMember(memberFormDto, Role.ADMIN);
    return ApiResponse.createSuccessMessage(member, SIGNUP_SUCCESS);
  }

  @PostMapping("/sign-in")
  public ApiResponse<?> signIn (@Valid @RequestBody SignInForm signInForm) {
    SignInResponse signInResponse = memberService.signInMember(signInForm);
    return ApiResponse.createSuccessMessage(signInResponse, SIGN_IN_SUCCESS);
  }

  @PostMapping("/reset-password")
  public ApiResponse<?> recreatePassword(@Valid @RequestBody EmailDto emailDto){
    SimpleMailMessage message = memberService.sendNewPassword(emailDto);
    return ApiResponse.createSuccessMessage(message, SEND_EMAIL);
  }
}
