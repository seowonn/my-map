package com.seowonn.mymap.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserInfoForm {

  @Email(message = "이메일 형식으로 입력해주세요.")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String currentId;
  @Email(message = "이메일 형식으로 입력해주세요.")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String newId;

  @NotNull(message = "현재 비밀번호를 입력해주세요.")
  private String currentPassword;
  @NotBlank(message = "새롭게 변경할 비밀번호를 입력해주세요.")
  private String newPassword;

  @NotBlank(message = "새로운 아이디로 받은 인증번호를 입력해주세요.")
  private String verificationNum;

}
