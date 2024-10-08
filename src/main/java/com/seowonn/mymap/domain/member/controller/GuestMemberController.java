package com.seowonn.mymap.domain.member.controller;

import static com.seowonn.mymap.global.type.SuccessMessage.SEND_EMAIL;
import static com.seowonn.mymap.global.type.SuccessMessage.SIGNUP_SUCCESS;
import static com.seowonn.mymap.global.type.SuccessMessage.SIGN_IN_SUCCESS;

import com.seowonn.mymap.domain.member.dto.*;
import com.seowonn.mymap.domain.member.service.MemberService;
import com.seowonn.mymap.global.dto.ApiResponse;
import com.seowonn.mymap.domain.email.dto.EmailDto;
import com.seowonn.mymap.domain.member.type.Role;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
public class GuestMemberController {

  // TODO : 리팩토링

  private final MemberService memberService;

  @Operation(summary = "일반 사용자 회원가입",
    description = "이메일로 인증해서 role.USER로 회원가입합니다.")
  @PostMapping("/standard/signup")
  public ApiResponse<?> signUpStandardUser(
      @Valid @RequestBody SignUpDto.SignUpRequest signUpRequest){
    SignUpDto.SignUpResponse signUpResponse =
        memberService.createMember(signUpRequest, Role.USER);
    return ApiResponse.createSuccessMessage(signUpResponse, SIGNUP_SUCCESS);
  }

  @Operation(summary = "관리자 사용자 회원가입",
      description = "이메일로 인증해서 role.ADMIN으로 회원가입합니다.")
  @PostMapping("/admin/signup")
  public ApiResponse<?> signUpAdmin (
      @Valid @RequestBody SignUpDto.SignUpRequest signUpRequest){
    SignUpDto.SignUpResponse signUpResponse =
        memberService.createMember(signUpRequest, Role.ADMIN);
    return ApiResponse.createSuccessMessage(signUpResponse, SIGNUP_SUCCESS);
  }

  @Operation(summary = "사용자 로그인",
      description = "아이디(이메일)과 비밀번호로 로그인을 하고 JWT 토큰을 발급 받습니다.")
  @PostMapping("/sign-in")
  public ApiResponse<?> signIn (@Valid @RequestBody LoginDto.LoginRequest loginRequest) {
    LoginDto.LoginResponse loginResponse = memberService.signInMember(loginRequest);
    return ApiResponse.createSuccessMessage(loginResponse, SIGN_IN_SUCCESS);
  }

  @Operation(summary = "사용자 비밀번호 재설정",
      description = "비밀번호를 잊어버렸을 경우 임시 비밀번호로 초기화 후 작성한"
          + "이메일로 보내줍니다.")
  @PostMapping("/reset-password")
  public ApiResponse<?> recreatePassword(@Valid @RequestBody EmailDto emailDto){
    SimpleMailMessage message = memberService.sendNewPassword(emailDto);
    return ApiResponse.createSuccessMessage(message, SEND_EMAIL);
  }
}
