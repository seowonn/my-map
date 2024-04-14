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
public class SiDoDto {

  private String districtCode;
  private String cityName;

  public static SiDoDto from(JSONObject property){
    return SiDoDto.builder()
        .districtCode(property.get("ctprvn_cd").toString())
        .cityName(property.get("ctp_kor_nm").toString())
        .build();
  }
}
