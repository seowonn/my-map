package com.seowonn.mymap.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SignInResponse {
  private String accessToken;
}
