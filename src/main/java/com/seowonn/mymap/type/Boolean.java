package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Boolean {

  TRUE("true"),
  FALSE("false");

  private final String flag;
}
