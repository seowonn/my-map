package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.DELETE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_CREATED;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.service.Impl.VisitLogServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log/{myMapId}") // 마이맵 선택 후 작성
public class VisitLogController {

  private final VisitLogServiceImpl visitLogService;

  /**
   * 작성할 마이맵 창에 들어가서 방문일지를 작성하는 api
   * siGunGuCode는 /search/{siDoCode} api에서 SiGunGu 정보로 확인
   * @ModelAttribute form-data로 자동 NewVisitLogDto로 매핑
   */
  @PostMapping("/new-log")
  public ApiResponse<?> createLog(
      @PathVariable Long myMapId,
      @Valid @ModelAttribute NewVisitLogDto newVisitLogDto){
    VisitLog visitLog =
        visitLogService.createVisitLog(myMapId, newVisitLogDto);
    return ApiResponse.createSuccessMessage(visitLog, VISIT_LOG_CREATED);
  }

  @GetMapping
  public ApiResponse<?> getVisitLogs(
      @PathVariable Long myMapId,
      @PageableDefault(page = 0, size = 10) Pageable pageable){
    Page<VisitLog> visitLogs = visitLogService.getVisitLogs(myMapId, pageable);
    return ApiResponse.createSuccessMessage(visitLogs, RETRIEVE_DATA_SUCCESS);
  }

  @DeleteMapping("/delete/{visitLogId}")
  public ApiResponse<?> deleteVisitLog(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId){
    visitLogService.deleteVisitLog(myMapId, visitLogId);
    return ApiResponse.createSuccessMessage(true, DELETE_SUCCESS);
  }

}
