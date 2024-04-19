package com.seowonn.mymap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Prefix {

  VERIFICATION_PREFIX("VERIFY:"),
  VIEW_COUNT_PREFIX("VISIT_LOG_VIEW:"),
  REDIS_LOCK_PREFIX("LOCK:");

  private final String prefix;
}
