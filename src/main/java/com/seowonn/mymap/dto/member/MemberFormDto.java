package com.seowonn.mymap.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberFormDto {
  private String userId;
  private String password;
  private String phone;
  private String username;
  private String gender;
  private String verificationNum;
}
