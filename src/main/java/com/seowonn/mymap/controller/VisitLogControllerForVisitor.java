package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_UPDATE_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.visitLog.VisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.service.Impl.VisitorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VisitLogControllerForVisitor {

  private final VisitorServiceImpl visitorService;

  @PostMapping("/logs/likes/{myMapId}/{visitLogId}")
  public ApiResponse<?> addLikes(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId) {
    VisitLog visitLog = visitorService.addVisitLogLikes(myMapId, visitLogId);
    return ApiResponse.createSuccessMessage(visitLog, VISIT_LOG_UPDATE_SUCCESS);
  }

  @DeleteMapping("/logs/likes/{myMapId}/{visitLogId}")
  public ApiResponse<?> deleteLikes(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId) {
    VisitLog visitLog = visitorService.deleteVisitLogLikes(myMapId, visitLogId);
    return ApiResponse.createSuccessMessage(visitLog, VISIT_LOG_UPDATE_SUCCESS);
  }

  /**
   * 마이맵 세부보기 방문일지들이 썸네일 형식으로 보여진다.
   */
  @GetMapping("/maps/{mayMapId}")
  public ApiResponse<?> getAllVisitLogsFromMyMap(
      @PathVariable Long mayMapId,
      @PageableDefault(page = 0, size = 20)
Pageable pageable) {
    Page<VisitLogDto> visitLogDtoPage =
        visitorService.getAllVisitLogsFromMyMap(mayMapId, pageable);

    return ApiResponse.createSuccessMessage(visitLogDtoPage,
        RETRIEVE_DATA_SUCCESS);
  }

}
