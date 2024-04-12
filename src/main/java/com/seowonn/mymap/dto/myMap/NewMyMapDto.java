package com.seowonn.mymap.dto.myMap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NewMyMapDto {

  @NotBlank(message = "사용자 아이디를 입력해주세요.")
  @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$",
      message = "이메일 주소 양식을 확인해주세요")
  private String userId;

  @NotBlank(message = "작성할 지역을 선택해주세요.")
  private String siDoCode;

  /**
   * TODO : myMapTitle이 빈칸으로 들어왔을 때, 기본 값을 지역명(광역시도)로 설정할 수 있도록 하기
   */
  @Size(min = 0, max = 20)
  private String myMapTitle;

  @NotBlank(message = "마이맵 공개 여부를 선택해주세요.")
  @Pattern(regexp = "public|private", message = "공개여부는 'private' 또는 'public' 이어야 합니다.")
  private String isPublic;
}
