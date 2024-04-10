package com.seowonn.mymap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewVisitLogDto {

  @NotBlank(message = "방문 지역을 입력해주세요.")
  private String siGunGuCode;

  @NotBlank(message = "방문한 곳의 장소를 알려주세요.")
  private String placeName;

  @Size(max = 1000)
  private String content;

  @NotBlank(message = "방문일지 공개 여부를 설정해주세요.")
  private String isPublic;

  private int recommendOrder;

}
