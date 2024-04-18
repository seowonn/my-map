package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_UPDATE_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.BookMarkDto;
import com.seowonn.mymap.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.dto.visitLog.VisitLogUserInputForm;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.service.Impl.VisitorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VisitLogControllerForVisitor {

  private final VisitorServiceImpl visitorService;

  /**
   * 방문일지 상세보기
   */
  @GetMapping("/logs/{myMapId}/{visitLogId}")
  public ApiResponse<?> getVisitLogDetails(@PathVariable Long visitLogId){
    VisitLogResponse visitLogResponse =
        visitorService.getVisitLogDetails(visitLogId);
    return ApiResponse.createSuccessMessage(visitLogResponse, RETRIEVE_DATA_SUCCESS);
  }

  /**
   * 방문일지 좋아요 & 북마크 마킹 기능
   */
  @PostMapping("/logs/{myMapId}/{visitLogId}")
  public ApiResponse<?> applyUserInput(
      @PathVariable Long myMapId, @PathVariable Long visitLogId,
      @Valid @RequestBody VisitLogUserInputForm form) {
    VisitLogResponse visitLogResponse =
        visitorService.applyUserInput(myMapId, visitLogId, form);
    return ApiResponse.createSuccessMessage(visitLogResponse, VISIT_LOG_UPDATE_SUCCESS);
  }

  /**
   * 북마크 목록 보기
   */
  @GetMapping("/logs/marks")
  public ApiResponse<?> getMarkedLogs(
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    Page<BookMarkDto> bookMarkDtoPage = visitorService.getMarkedLogs(pageable);
    return ApiResponse.createSuccessMessage(bookMarkDtoPage, RETRIEVE_DATA_SUCCESS);
  }

  /**
   * 마이맵 세부보기 방문일지들이 썸네일 형식으로 보여진다.
   */
  @GetMapping("/maps/{mayMapId}")
  public ApiResponse<?> getAllVisitLogsFromMyMap(
      @PathVariable Long mayMapId,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    Page<VisitLogResponse> visitLogDtoPage =
        visitorService.getAllVisitLogsFromMyMap(mayMapId, pageable);

    return ApiResponse.createSuccessMessage(visitLogDtoPage,
        RETRIEVE_DATA_SUCCESS);
  }

}
