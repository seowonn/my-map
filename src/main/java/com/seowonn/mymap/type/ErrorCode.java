package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  USERID_EXISTS("중복되는 아이디가 존재합니다."),
  EMAIL_SEND_ERROR("이메일 전송 과정 중 에러가 발생하였습니다."),
  EXPIRED_VERIFICATION("만료된 인증 번호입니다."),
  INCORRECT_CODE("올바르지 않은 인증 번호입니다."),
  INCORRECT_EMAIL("인증 번호를 전송한 이메일을 입력해주세요."),
  INVALID_SERVER_ERROR("내부 서버 오류가 발생하였습니다.");

  private final String description;
}
