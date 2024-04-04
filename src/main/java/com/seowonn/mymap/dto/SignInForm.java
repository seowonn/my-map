package com.seowonn.mymap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInForm {
  private String userId;
  private String password;
}
