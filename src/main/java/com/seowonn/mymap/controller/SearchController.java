package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoResponse;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuResponse;
import com.seowonn.mymap.entity.elasticDB.VisitLogDocument;
import com.seowonn.mymap.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;

  @Operation(summary = "광역시도 목록 조회")
  @GetMapping("/city")
  public ApiResponse<?> getCities(
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<SiDoResponse> siDoDtoPage = searchService.getSiDoCites(pageable);
    return ApiResponse.createSuccessMessage(siDoDtoPage, RETRIEVE_DATA_SUCCESS);
  }

  @Operation(summary = "시군구 목록 조회",
      description = "광역시도 코드를 통해 해당 지역 내 시군구 정보를 보여줍니다.")
  @GetMapping("/{siDoCode}/small-city")
  public ApiResponse<?> getCities(
      @PathVariable String siDoCode,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<SiGunGuResponse> siGunGuDtoPage = searchService.getSiGunGuCites(siDoCode, pageable);
    return ApiResponse.createSuccessMessage(siGunGuDtoPage, RETRIEVE_DATA_SUCCESS);
  }

  @Operation(summary = "검색어로 방문일지 목록 조회",
      description = "방문일지의 장소명과 내용에서 검색어와 접두사가 같은 검색 결과들이 나옵니다.")
  @GetMapping("/logs/{keyword}")
  public ApiResponse<?> searchLogs(
      @PathVariable String keyword,
      @PageableDefault(page = 0, size = 20) Pageable pageable){
    Page<VisitLogDocument> visitLogDocumentPage =
        searchService.searchVisitLogByKeyword(keyword, pageable);
    return ApiResponse.createSuccessMessage(visitLogDocumentPage, RETRIEVE_DATA_SUCCESS);
  }
}
