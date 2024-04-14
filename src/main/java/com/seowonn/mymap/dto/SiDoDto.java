package com.seowonn.mymap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SiDoDto {

  private String ctprvn_cd; // districtCode
  private String ctp_kor_nm; // cityName

}
