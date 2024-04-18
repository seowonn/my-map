package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoResponse;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuResponse;
import com.seowonn.mymap.service.Impl.SearchServiceImpl;
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

  private final SearchServiceImpl searchService;

  @GetMapping("/city")
  public ApiResponse<?> getCities(
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<SiDoResponse> siDoDtoPage = searchService.getSiDoCites(pageable);
    return ApiResponse.createSuccessMessage(siDoDtoPage, RETRIEVE_DATA_SUCCESS);
  }

  @GetMapping("/{siDoCode}/small-city")
  public ApiResponse<?> getCities(
      @PathVariable String siDoCode,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<SiGunGuResponse> siGunGuDtoPage = searchService.getSiGunGuCites(siDoCode, pageable);
    return ApiResponse.createSuccessMessage(siGunGuDtoPage, RETRIEVE_DATA_SUCCESS);
  }
}
