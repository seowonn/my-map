package com.seowonn.mymap.dto.myMap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMyMapDto {

  @Size(min = 0, max = 20)
  private String myMapTitle;

  @NotBlank(message = "마이맵 공개 여부를 선택해주세요.")
  private String isPublic;

}
