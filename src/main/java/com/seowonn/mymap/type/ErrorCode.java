package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  USERID_EXISTS("중복되는 아이디가 존재합니다."),
  EMAIL_SEND_ERROR("이메일 전송 과정 중 에러가 발생하였습니다."),
  INVALID_SERVER_ERROR("내부 서버 오류가 발생하였습니다.");

  private final String description;
}
