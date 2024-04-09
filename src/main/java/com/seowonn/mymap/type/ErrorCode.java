package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INVALID_PARAMETER("유효하지 않은 입력형식입니다."),
  USER_NOT_FOUND("해당 아이디의 사용자가 존재하지 않습니다."),
  INCORRECT_PASSWORD("올바르지 않은 비밀번호입니다."),
  USERID_EXISTS("중복되는 아이디가 존재합니다."),
  EMAIL_SEND_ERROR("이메일 전송 과정 중 에러가 발생하였습니다."),
  EXPIRED_VERIFICATION("만료된 인증 번호입니다."),
  INCORRECT_CODE("올바르지 않은 인증 번호입니다."),
  INCORRECT_EMAIL("인증 번호를 전송한 이메일을 입력해주세요."),
  TOKEN_ACCESS_DENIED("jwt 토큰 접근 권한이 거부되었습니다."),
  UNAUTHORIZED("인증되지 않은 사용자입니다."),
  ACCESS_DENIED("접근 권한이 없는 사용자입니다."),
  DATA_SCRAPPING_ERROR("데이터를 가져오는데 오류가 발생하였습니다."),
  MY_MAP_NOT_FOUND("해당 아이디로 등록된 마이맵이 없습니다."),
  INVALID_SERVER_ERROR("내부 서버 오류가 발생하였습니다.");

  private final String description;
}
