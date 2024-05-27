package com.seowonn.mymap.domain.visitLogForVisitor.controller;

import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_UPDATE_SUCCESS;

import com.seowonn.mymap.global.dto.ApiResponse;
import com.seowonn.mymap.domain.member.dto.bookMark.BookMarkDto;
import com.seowonn.mymap.domain.visitLogForWriter.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.domain.visitLogForWriter.dto.visitLog.VisitLogUserInputForm;
import com.seowonn.mymap.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
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

  private final VisitorService visitorService;

  @Operation(summary = "방문일지 상세보기",
      description = "로그인 아이디 당 조회수 1이 카운트됩니다.")
  @GetMapping("/logs/{myMapId}/{visitLogId}")
  public ApiResponse<?> getVisitLogDetails(@PathVariable Long visitLogId){
    VisitLogResponse visitLogResponse =
        visitorService.getVisitLogDetails(visitLogId);
    return ApiResponse.createSuccessMessage(visitLogResponse, RETRIEVE_DATA_SUCCESS);
  }

  @Operation(summary = "방문일지 좋아요 & 북마크 설정")
  @PostMapping("/logs/{myMapId}/{visitLogId}")
  public ApiResponse<?> applyUserInput(
      @PathVariable Long myMapId, @PathVariable Long visitLogId,
      @Valid @RequestBody VisitLogUserInputForm form) {
    VisitLogResponse visitLogResponse =
        visitorService.applyUserInput(myMapId, visitLogId, form);
    return ApiResponse.createSuccessMessage(visitLogResponse, VISIT_LOG_UPDATE_SUCCESS);
  }

  @Operation(summary = "북마크로 선택한 방문일지 목록 전체 조회")
  @GetMapping("/logs/marks")
  public ApiResponse<?> getMarkedLogs(
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    Page<BookMarkDto> bookMarkDtoPage = visitorService.getMarkedLogs(pageable);
    return ApiResponse.createSuccessMessage(bookMarkDtoPage, RETRIEVE_DATA_SUCCESS);
  }

  @Operation(summary = "북마크로 선택한 방문일지 목록 카테고려별 조회")
  @GetMapping("/logs/marks/{categoryName}")
  public ApiResponse<?> getCategoryMarkedLogs(
      @PathVariable String categoryName,
      @PageableDefault(page = 0, size = 20)Pageable pageable) {
    Page<BookMarkDto> bookMarkDtoPage =
        visitorService.getCategoryMarkedLogs(categoryName, pageable);
    return ApiResponse.createSuccessMessage(bookMarkDtoPage, RETRIEVE_DATA_SUCCESS);
  }

  @Operation(summary = "마이맵에 등록된 방문일지 목록 보기")
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
