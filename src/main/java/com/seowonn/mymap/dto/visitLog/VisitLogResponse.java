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
public class VisitLogResponse {

  private String siGunGu;
  private String placeName;
  private String content;
  private String imageFileUrl;
  private Integer recommendOrder;
  private Integer likes;
  private String isMarked;
  private Integer viewCount;
  private LocalDateTime createdAt;

  public static Page<VisitLogResponse> fromPage(Page<VisitLog> visitLogPage){
    return visitLogPage.map(m -> VisitLogResponse.builder()
        .siGunGu(m.getSiGunGu().getSiGunGuName())
        .placeName(m.getPlaceName())
        .content(m.getContent())
        .content(m.getContent())
        .imageFileUrl(m.getImages().get(0).getImageUrl())
        .recommendOrder(m.getRecommendOrder())
        .likes(m.getLikes())
        .viewCount(m.getViews())
        .createdAt(m.getCreatedAt())
        .build());
  }

  public static VisitLogResponse from(VisitLog visitLog, String isMarked){
    return VisitLogResponse.builder()
        .siGunGu(visitLog.getSiGunGu().getSiGunGuName())
        .placeName(visitLog.getPlaceName())
        .content(visitLog.getContent())
        .imageFileUrl(visitLog.getImages().get(0).getImageUrl())
        .recommendOrder(visitLog.getRecommendOrder())
        .isMarked(isMarked)
        .viewCount(visitLog.getViews())
        .createdAt(visitLog.getCreatedAt())
        .build();
  }

}
