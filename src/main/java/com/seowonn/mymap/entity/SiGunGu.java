package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seowonn.mymap.dto.SiGunGuDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
  @JsonBackReference
  private SiDo siDo;

  public static SiGunGu from(SiGunGuDto siGunGuDto) {
    return SiGunGu.builder()
        .siGunGuName(siGunGuDto.getSig_kor_nm())
        .siGunGuCode(siGunGuDto.getSig_cd())
        .build();
  }

}
