package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.DELETE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_CREATED;
import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_UPDATE_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.service.S3Service;
import com.seowonn.mymap.service.VisitLogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logs") // 마이맵 선택 후 작성
public class VisitLogControllerForWriter {

  private final VisitLogService visitLogService;
  private final S3Service s3Service;

  @Operation(summary = "방문일지 생성",
      description = "마이맵을 선택하여 광역시도를 특정 짓고,"
          + "search에서 시군구 코드를 찾아 작성하여 세부 지역을 특정 짓습니다.")
  @PostMapping("/{myMapId}")
  public ApiResponse<?> createLog(
      @PathVariable Long myMapId,
      @Valid @ModelAttribute NewVisitLogDto newVisitLogDto){
    VisitLogResponse visitLogResponse =
        visitLogService.createVisitLog(myMapId, newVisitLogDto);
    return ApiResponse.createSuccessMessage(visitLogResponse, VISIT_LOG_CREATED);
  }

  @Operation(summary = "방문일지 목록 조회",
      description = "작성자가 해당 마이맵에 작성해온 방문일지들을 조회할 수 있습니다.")
  @GetMapping("/{myMapId}")
  public ApiResponse<?> getUsersVisitLogs(
      @PathVariable Long myMapId,
      @PageableDefault(page = 0, size = 10) Pageable pageable){
    Page<VisitLogResponse> visitLogPage = visitLogService.getUsersVisitLogs(myMapId, pageable);
    return ApiResponse.createSuccessMessage(visitLogPage, RETRIEVE_DATA_SUCCESS);
  }

  @Operation(summary = "방문일지 수정",
      description = "장소명, 내용, 카테고리, 공개 여부를 수정할 수 있으며"
          + "사진은 삭제할 사진들의 url을 받아, 삭제만 가능합니다.")
  @PutMapping("/{myMapId}/{visitLogId}")
  public ApiResponse<?> updateVisitLog(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId,
      @Valid @ModelAttribute UpdateVisitLogDto updateVisitLogDto
  ){
    VisitLogResponse visitLogResponse =
        visitLogService.updateLog(myMapId, visitLogId, updateVisitLogDto);
    return ApiResponse.createSuccessMessage(visitLogResponse, VISIT_LOG_UPDATE_SUCCESS);
  }

  @Operation(summary = "방문일지 삭제")
  @DeleteMapping("/{myMapId}/{visitLogId}")
  public ApiResponse<?> deleteVisitLog(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId){
    List<String> imageUrlsToDelete = visitLogService.deleteVisitLogGetDeleteUrls(myMapId, visitLogId);
    for (String url : imageUrlsToDelete){
      s3Service.deleteS3File(url);
    }
    return ApiResponse.createSuccessMessage(true, DELETE_SUCCESS);
  }

}
