package com.seowonn.mymap.infra.email.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
  
  @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$",
      message = "이메일 주소 양식을 확인해주세요")
  @NotBlank(message = "반드시 내용을 입력해야 합니다.")
  private String emailAddress;
}
