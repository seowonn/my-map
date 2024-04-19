package com.seowonn.mymap.dto.member;

import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.type.Gender;
import com.seowonn.mymap.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

  private Role role;
  private String userId;
  private String phone;
  private String userName;
  private Gender gender;

  public static MemberResponse from(Member member){
    return MemberResponse.builder()
        .role(member.getRole())
        .userId(member.getUserId())
        .phone(member.getPhone())
        .userName(member.getUserName())
        .gender(member.getGender())
        .build();
  }

}
