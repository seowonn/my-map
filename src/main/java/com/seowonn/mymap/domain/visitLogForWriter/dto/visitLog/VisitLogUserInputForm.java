package com.seowonn.mymap.domain.visitLogForWriter.dto.visitLog;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VisitLogUserInputForm {

  @Pattern(regexp = "true|false", message = "좋아요는 'true' 또는 'false' 형태여야 합니다.")
  private String liked;

  @Pattern(regexp = "true|false", message = "북마크는 'true' 또는 'false' 형태여야 합니다.")
  private String marked;
}
