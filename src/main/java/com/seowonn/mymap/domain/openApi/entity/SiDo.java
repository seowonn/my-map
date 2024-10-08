package com.seowonn.mymap.domain.openApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seowonn.mymap.domain.openApi.dto.siDo.SiDoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "siDo")
@EntityListeners(AuditingEntityListener.class)
public class SiDo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String siDoName;

  @Column(nullable = false, unique = true)
  private String siDoCode;

  @OneToMany(mappedBy = "siDo", fetch = FetchType.LAZY)
  @ToString.Exclude
  @Builder.Default
  @JsonIgnore
  private List<SiGunGu> siGunGuList = new ArrayList<>();

  public static SiDo from(SiDoResponse sidoResponse) {
    return SiDo.builder()
        .siDoName(sidoResponse.getCtp_kor_nm())
        .siDoCode(sidoResponse.getCtprvn_cd())
        .build();
  }

}
