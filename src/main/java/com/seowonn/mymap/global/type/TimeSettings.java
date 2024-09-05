package com.seowonn.mymap.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeSettings {

  VERIFICATION_EXPIRE_TIME(60L * 5), // 5분
  VIEWS_COUNT_EXPIRE_TIME(60L * 15),  // 15분
  ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 30);

  private final long time;

}
