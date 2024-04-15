package com.seowonn.mymap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVisitLogDto {

  @NotBlank(message = "방문한 곳의 장소를 알려주세요.")
  private String placeName;

  @Size(max = 1000)
  private String content;

  @NotBlank(message = "방문일지 공개 여부를 선택해주세요.")
  @Pattern(regexp = "public|private", message = "공개여부는 'private' 또는 'public' 이어야 합니다.")
  private String access;

  private int recommendOrder;

  private List<String> deleteFileUrls;

}
