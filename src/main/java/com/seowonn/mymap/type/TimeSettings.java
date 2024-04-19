package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeSettings {

  VERIFICATION_EXPIRE_TIME(60L * 5),
  VIEWS_COUNT_EXPIRE_TIME(60 * 15),
  ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 30);

  private final long time;

}
