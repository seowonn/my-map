package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Prefix {

  VERIFICATION_PREFIX("VERIFY:"),
  VIEW_COUNT_PREFIX("VISIT_LOG_VIEW:");

  private final String prefix;
}
