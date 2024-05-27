package com.seowonn.mymap.domain.member.dto.member;

import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.member.type.Gender;
import com.seowonn.mymap.type.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberInfoResponse {

  private String userId;
  private String phone;
  private String userName;
  private Gender gender;
  private Role role;
  private LocalDateTime createdAt;

  public static MemberInfoResponse from(Member member){
    return MemberInfoResponse.builder()
        .userId(member.getUserId())
        .phone(member.getPhone())
        .userName(member.getUserName())
        .gender(member.getGender())
        .role(member.getRole())
        .createdAt(member.getCreatedAt())
        .build();
  }

}
