package com.seowonn.mymap.dto;

import com.seowonn.mymap.entity.BookMarks;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookMarkDto {

  private String siGunGu;
  private String placeName;
  private LocalDateTime createdAt;

  public static Page<BookMarkDto> toDtoList(Page<BookMarks> bookMarksPage){
    return bookMarksPage.map(m -> BookMarkDto.builder()
        .siGunGu(m.getVisitLog().getSiGunGu().getSiGunGuName())
        .placeName(m.getVisitLog().getPlaceName())
        .createdAt(m.getCreatedAt())
        .build());
  }
}
