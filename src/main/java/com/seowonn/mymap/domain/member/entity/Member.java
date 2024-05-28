package com.seowonn.mymap.domain.member.entity;

import com.seowonn.mymap.domain.member.dto.MemberFormDto;
import com.seowonn.mymap.global.entity.BaseEntity;
import com.seowonn.mymap.domain.myMap.entity.MyMap;
import com.seowonn.mymap.domain.member.type.Gender;
import com.seowonn.mymap.domain.member.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = false, unique = true)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String userName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  @ToString.Exclude
  @Builder.Default
  private List<MyMap> myMapList = new ArrayList<>();

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  @ToString.Exclude
  @Builder.Default
  private List<BookMarks> bookMarksList = new ArrayList<>();

  public static Member ofMemberFormAndRole(MemberFormDto memberFormDto, Role role) {

    Gender gender = Gender.valueOf(memberFormDto.getGender().toUpperCase());

    String encPassword =
        BCrypt.hashpw(memberFormDto.getPassword(), BCrypt.gensalt());

    return Member.builder()
        .role(role)
        .userId(memberFormDto.getUserId())
        .password(encPassword)
        .phone(memberFormDto.getPhone())
        .userName(memberFormDto.getUsername())
        .gender(gender)
        .build();
  }

}
