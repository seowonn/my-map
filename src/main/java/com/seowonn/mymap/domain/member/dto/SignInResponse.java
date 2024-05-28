package com.seowonn.mymap.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SignInResponse {
  private String accessToken;
}
