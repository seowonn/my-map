package com.seowonn.mymap.dto.cityOpenApi.siDo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seowonn.mymap.entity.SiDo;
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
public class SiDoResponse {

  @JsonProperty("districtCode")
  private String ctprvn_cd;

  @JsonProperty("cityName")
  private String ctp_kor_nm;

  public static Page<SiDoResponse> fromPage(Page<SiDo> siDoPage){
    return siDoPage.map(siDo -> SiDoResponse.builder()
        .ctprvn_cd(siDo.getSiDoCode())
        .ctp_kor_nm(siDo.getSiDoName())
        .build());
  }

}
