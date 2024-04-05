package com.seowonn.mymap.dto.member;

import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.type.Gender;
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
public class MemberInfoDto {

  private String userId;
  private String phone;
  private String userName;
  private Gender gender;
  private Role role;
  private LocalDateTime createdAt;

  public static MemberInfoDto entityToDto(Member member){
    return MemberInfoDto.builder()
        .userId(member.getUserId())
        .phone(member.getPhone())
        .userName(member.getUserName())
        .gender(member.getGender())
        .role(member.getRole())
        .createdAt(member.getCreatedAt())
        .build();
  }

}
