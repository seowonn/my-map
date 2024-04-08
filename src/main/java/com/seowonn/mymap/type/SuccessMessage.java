package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

  SEND_EMAIL("해당 이메일로 인증번호를 전송하였습니다."),
  SIGNUP_SUCCESS("회원 가입을 성공적으로 완료하였습니다."),
  USER_PROFILE_VIEWED("회원 정보가 정상적으로 조회되었습니다."),
  PROFILE_UPDATE_SUCCESS("회원 정보를 정상적으로 수정하였습니다."),
  SIGN_OUT_SUCCESS("회원 탈퇴를 정상적으로 수행하였습니다."),
  SIGN_IN_SUCCESS("로그인을 성공적으로 완료하였습니다.");

  private final String description;
}
