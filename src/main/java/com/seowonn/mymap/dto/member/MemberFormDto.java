package com.seowonn.mymap.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberFormDto {
  @Email(message = "이메일 형식으로 입력해주세요.")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String userId;

  @NotBlank(message = "비밀번호는 필수입니다.")
  private String password;

  @NotBlank(message = "전화번호는 필수입니다.")
  private String phone;

  @NotBlank(message = "활동명을 입력해야 합니다.")
  private String username;

  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  @Pattern(regexp = "male|female", message = "gender는 'male' 또는 'female' 이어야 합니다.")
  private String gender;

  @NotBlank(message = "등록할 아이디로 받은 인증번호를 입력해주세요.")
  private String verificationNum;
}
