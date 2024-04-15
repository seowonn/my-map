package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeSettings {

  VERIFICATION_EXPIRE_TIME(60 * 30),
  VIEWS_COUNT_EXPIRE_TIME(60 * 15);

  private final long time;

}
