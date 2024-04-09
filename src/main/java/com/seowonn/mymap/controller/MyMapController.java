package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.MY_MAP_CREATED;
import static com.seowonn.mymap.type.SuccessMessage.MY_MAP_UPDATE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.MyMap.NewMyMapDto;
import com.seowonn.mymap.dto.MyMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.service.Impl.MyMapServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-map")
public class MyMapController {

  private final MyMapServiceImpl myMapService;

  @GetMapping("/city")
  public ApiResponse<?> getCities(
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<SiDo> citiesDto = myMapService.getSiDoCites(pageable);
    return ApiResponse.createSuccessMessage(citiesDto, RETRIEVE_DATA_SUCCESS);
  }

  @PostMapping("/new-map")
  public ApiResponse<?> createMyMap(@Valid @RequestBody NewMyMapDto newMyMapDto) {
    MyMap myMap = myMapService.registerNewMap(newMyMapDto);
    return ApiResponse.createSuccessMessage(myMap, MY_MAP_CREATED);
  }

  @PatchMapping("/edit-map/{userId}")
  public ApiResponse<?> updateMyMap(
      @Valid @RequestBody UpdateMyMapDto updateMyMapDto,
      @PathVariable String userId
  ){
    MyMap myMap = myMapService.updateMyMap(updateMyMapDto, userId);
    return ApiResponse.createSuccessMessage(myMap, MY_MAP_UPDATE_SUCCESS);
  }
}
