package com.seowonn.mymap.dto.cityOpenApi.siGunGu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seowonn.mymap.entity.SiGunGu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SiGunGuResponse {

  private String sig_cd; // districtCode

  private String full_nm;

  private String sig_kor_nm; // smallCityName

  public static Page<SiGunGuResponse> fromPage(Page<SiGunGu> siGunGuPage){
    return siGunGuPage.map(siGunGu -> SiGunGuResponse.builder()
        .sig_cd(siGunGu.getSiGunGuCode())
        .sig_kor_nm(siGunGu.getSiGunGuName())
        .build());
  }

}
