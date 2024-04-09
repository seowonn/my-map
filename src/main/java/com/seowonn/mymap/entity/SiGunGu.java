package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seowonn.mymap.dto.SiGunGuDto;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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

  @Column(nullable = false)
  private String siGunGuCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "siDo",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  @JsonBackReference
  private SiDo siDo;

  public static SiGunGu buildFromDto(SiGunGuDto siGunGuDto) {
    return SiGunGu.builder()
        .siGunGuName(siGunGuDto.getSmallCityName())
        .siGunGuCode(siGunGuDto.getDistrictCode())
        .build();
  }

}
