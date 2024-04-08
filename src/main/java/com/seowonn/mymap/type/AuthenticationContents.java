package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationContents {

  EMAIL_AUTH_TITLE("[인증 번호 발송] : 인증 번호를 확인해주세요");

  private final String message;

}
