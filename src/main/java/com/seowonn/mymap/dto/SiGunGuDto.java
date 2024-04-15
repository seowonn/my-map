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

  private String sig_cd; // districtCode
  private String full_nm; // cityFullName
  private String sig_kor_nm; // smallCityName

  public static SiGunGuDto makeSiGGDto(JSONObject property){
    return SiGunGuDto.builder()
        .sig_cd(property.get("sig_cd").toString())
        .full_nm(property.get("full_nm").toString())
        .sig_kor_nm(property.get("sig_kor_nm").toString())
        .build();
  }

}
