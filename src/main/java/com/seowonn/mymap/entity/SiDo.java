package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seowonn.mymap.dto.SiDoDto;
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

  @Column(nullable = false, unique = true)
  private String siDoName;

  @Column(nullable = false, unique = true)
  private String siDoCode;

  @OneToMany(mappedBy = "siDo", fetch = FetchType.EAGER)
  @ToString.Exclude
  @Builder.Default
  /**
   * TODO : siGunGuList가 필요한 상황이 있는지 셍각해보기
   */
  @JsonIgnore
  @JsonManagedReference
  private List<SiGunGu> siGunGuList = new ArrayList<>();

  public static SiDo buildFromDto(SiDoDto sidoDto) {
    return SiDo.builder()
        .siDoName(sidoDto.getCityName())
        .siDoCode(sidoDto.getDistrictCode())
        .build();
  }

}
