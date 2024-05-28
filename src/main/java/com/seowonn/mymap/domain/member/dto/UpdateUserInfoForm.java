package com.seowonn.mymap.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserInfoForm {

  @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$",
      message = "이메일 주소 양식을 확인해주세요")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String newId;

  @NotNull(message = "현재 비밀번호를 입력해주세요.")
  private String currentPassword;
  @NotBlank(message = "새롭게 변경할 비밀번호를 입력해주세요.")
  private String newPassword;

  @NotBlank(message = "새로운 아이디로 받은 인증번호를 입력해주세요.")
  private String verificationNum;

}
