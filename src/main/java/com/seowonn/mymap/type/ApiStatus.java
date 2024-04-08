package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiStatus {
  SUCCESS_STATUS("success"),
  FAIL_STATUS ("fail");

  private final String status;
}
