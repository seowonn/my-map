package com.seowonn.mymap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
  @Email(message = "이메일 형식으로 입력해주세요.")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String emailAddress;
}
