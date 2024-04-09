package com.seowonn.mymap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SiGunGuDto {

  private String districtCode;
  private String cityFullName;
  private String smallCityName;

  public static SiGunGuDto makeSiggDto(JSONObject property){
    return SiGunGuDto.builder()
        .districtCode(property.get("sig_cd").toString())
        .cityFullName(property.get("full_nm").toString())
        .smallCityName(property.get("sig_kor_nm").toString())
        .build();
  }

}
