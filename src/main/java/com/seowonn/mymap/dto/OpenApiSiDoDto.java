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
public class OpenApiSiDoDto {

  private String districtCode;
  private String cityName;

  public static OpenApiSiDoDto makeSiDoDto(JSONObject property){
    return OpenApiSiDoDto.builder()
        .districtCode(property.get("ctprvn_cd").toString())
        .cityName(property.get("ctp_kor_nm").toString())
        .build();
  }
}
