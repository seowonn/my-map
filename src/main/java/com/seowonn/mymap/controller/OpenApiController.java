package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.DATA_LOAD_COMPLETE;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.OpenApiSiDoDto;
import com.seowonn.mymap.dto.OpenApiSiggDto;
import com.seowonn.mymap.service.Impl.OpenApiServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
public class OpenApiController {

  private final OpenApiServiceImpl openApiService;

  @GetMapping("/load/si-do")
  public ApiResponse<?> loadSiDoData() throws ParseException {
    List<OpenApiSiDoDto> openApiSiDoDto = openApiService.fetchSiDo();
    return ApiResponse.createSuccessMessage(openApiSiDoDto, DATA_LOAD_COMPLETE);
  }

  @GetMapping("/load/si-gun-gu")
  public ApiResponse<?> loadSiGunGuData() throws ParseException {
    List<OpenApiSiggDto> openApiSiGunGugDto = openApiService.fetchSiGunGu();
    return ApiResponse.createSuccessMessage(openApiSiGunGugDto, DATA_LOAD_COMPLETE);
  }

}
