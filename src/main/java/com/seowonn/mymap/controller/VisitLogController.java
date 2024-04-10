package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_CREATED;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.service.Impl.VisitLogServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log/{myMapId}") // 마이맵 선택 후 작성
public class VisitLogController {

  private final VisitLogServiceImpl visitLogService;

  /**
   * /search/{siDoCode} api에서 SiGunGu 정보 확인
   * 해당 정보 중 siGunGuCode를 NewVisitLogDto에 넣어서 post로 보냄
   * @PathVariable myMapId
   * @RequestBody newVisitLogDto
   * @return ApiResponse
   */
  @PostMapping("/new-log")
  public ApiResponse<?> createLog(
      @PathVariable Long myMapId,
      @Valid @RequestBody NewVisitLogDto newVisitLogDto
  ) {
    VisitLog visitLog =
        visitLogService.createVisitLog(myMapId, newVisitLogDto);
    return ApiResponse.createSuccessMessage(visitLog, VISIT_LOG_CREATED);
  }

}
