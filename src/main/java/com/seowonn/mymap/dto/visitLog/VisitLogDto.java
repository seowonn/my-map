package com.seowonn.mymap.dto.visitLog;

import com.seowonn.mymap.entity.VisitLog;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VisitLogDto {

  private String siGunGu;
  private String placeName;
  private String content;
  private String imageFileUrl;
  private Integer recommendOrder;
  private LocalDateTime createdAt;

  public static Page<VisitLogDto> toDtoList(Page<VisitLog> visitLogPage){
    return visitLogPage.map(m -> VisitLogDto.builder()
        .siGunGu(m.getSiGunGu().getSiGunGuName())
        .placeName(m.getPlaceName())
        .content(m.getContent())
        .content(m.getContent())
        .imageFileUrl(m.getImages().get(0).getImageUrl())
        .recommendOrder(m.getRecommendOrder())
        .createdAt(m.getCreatedAt())
        .build());
  }

}
