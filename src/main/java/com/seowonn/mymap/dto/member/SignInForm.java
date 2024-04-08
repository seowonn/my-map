package com.seowonn.mymap.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInForm {
  @Email(message = "이메일 형식으로 입력해주세요.")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String userId;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
}
