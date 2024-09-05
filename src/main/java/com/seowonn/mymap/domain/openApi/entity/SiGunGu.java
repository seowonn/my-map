package com.seowonn.mymap.domain.openApi.entity;

import com.seowonn.mymap.domain.openApi.dto.siGunGu.SiGunGuResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "siGunGu")
@EntityListeners(AuditingEntityListener.class)
public class SiGunGu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String siGunGuName;

  @Column(nullable = false, unique = true)
  private String siGunGuCode;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "siDo")
  private SiDo siDo;

  public static SiGunGu from(SiGunGuResponse siGunGuResponse) {
    return SiGunGu.builder()
        .siGunGuName(siGunGuResponse.getSig_kor_nm())
        .siGunGuCode(siGunGuResponse.getSig_cd())
        .build();
  }

}
