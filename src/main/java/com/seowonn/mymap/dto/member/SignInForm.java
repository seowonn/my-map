package com.seowonn.mymap.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInForm {
  private String userId;
  private String password;
}
