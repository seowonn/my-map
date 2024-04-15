package com.seowonn.mymap.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInForm {

  @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$",
      message = "이메일 주소 양식을 확인해주세요")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String userId;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
}
