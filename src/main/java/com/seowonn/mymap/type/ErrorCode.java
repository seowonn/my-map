package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INVALID_PARAMETER("유효하지 않은 입력형식입니다."),
  USER_NOT_FOUND("해당 아이디의 사용자가 존재하지 않습니다."),
  CATEGORY_NOT_FOUND("존재하지 않는 카테고리입니다."),
  INCORRECT_PASSWORD("올바르지 않은 비밀번호입니다."),
  USERID_EXISTS("중복되는 아이디가 존재합니다."),
  EMAIL_SEND_ERROR("이메일 전송 과정 중 에러가 발생하였습니다."),
  EXPIRED_VERIFICATION("만료된 인증 번호입니다."),
  INCORRECT_CODE("올바르지 않은 인증 번호입니다."),
  INCORRECT_EMAIL("인증 번호를 전송한 이메일을 입력해주세요."),
  REGION_NOT_FOUND("해당 지역으로 조회된 결과가 없습니다."),
  INCORRECT_REGION("지역 정보가 일치하지 않습니다."),
  TOKEN_ACCESS_DENIED("jwt 토큰 접근 권한이 거부되었습니다."),
  UNAUTHORIZED("인증되지 않은 사용자입니다."),
  ACCESS_DENIED("접근 권한이 없는 사용자입니다."),
  DATA_SCRAPPING_ERROR("데이터를 가져오는데 오류가 발생하였습니다."),
  MY_MAP_NOT_FOUND("해당 아이디로 등록된 마이맵이 없습니다."),
  VISIT_LOG_NOT_FOUND("해당 아이디로 등록된 방문일지가 없습니다."),
  IMAGE_NOT_FOUND("해당 방문일지에 해당 url로 조회되는 이미지가 없습니다."),
  LOADING_FILE_ERROR("파일 시스템이나 네트워크 상에 문제가 있습니다."),
  FILE_CONVERT_FAIL("파일 변환에 실패하였습니다."),
  FILES_EXCEED("파일 업로드 개수를 초과하였습니다."),
  NO_IMAGE_RECEIVED("전달 받은 이미지가 없습니다."),
  NO_LIKES_CLICKED("해당 방문일지에 좋아요를 누른 적이 없습니다."),
  CONNECTION_LOST("접속이 끊어졌습니다."),
  INVALID_SERVER_ERROR("내부 서버 오류가 발생하였습니다.");

  private final String description;
}
