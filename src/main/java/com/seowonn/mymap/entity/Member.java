package com.seowonn.mymap.entity;

import com.seowonn.mymap.dto.MemberFormDto;
import com.seowonn.mymap.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {

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
  private String gender;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  public static Member buildFromDto(MemberFormDto memberFormDto, Role role) {

    String encPassword =
        BCrypt.hashpw(memberFormDto.getPassword(), BCrypt.gensalt());

    return Member.builder()
        .role(role)
        .userId(memberFormDto.getUserId())
        .password(encPassword)
        .phone(memberFormDto.getPhone())
        .userName(memberFormDto.getUsername())
        .gender(memberFormDto.getGender())
        .build();
  }

}
