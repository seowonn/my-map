package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.DELETE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.MY_MAP_CREATED;
import static com.seowonn.mymap.type.SuccessMessage.MY_MAP_UPDATE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.RETRIEVE_DATA_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.myMap.MyMapResponse;
import com.seowonn.mymap.dto.myMap.NewMyMapDto;
import com.seowonn.mymap.dto.myMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.service.Impl.MyMapServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-map/maps")
public class MyMapController {

  private final MyMapServiceImpl myMapService;

  @PostMapping("/")
  public ApiResponse<?> createMyMap(@Valid @RequestBody NewMyMapDto newMyMapDto) {
    MyMapResponse myMapResponse =
        myMapService.registerNewMap(newMyMapDto);
    return ApiResponse.createSuccessMessage(myMapResponse, MY_MAP_CREATED);
  }

  @GetMapping("/{userId}")
  public ApiResponse<?> getAllMyMaps(
      @PathVariable String userId,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<MyMapResponse> myMapResponsePage =
        myMapService.getAllMyMaps(userId, pageable);
    return ApiResponse.createSuccessMessage(myMapResponsePage, RETRIEVE_DATA_SUCCESS);
  }

  @PatchMapping("/{myMapId}")
  public ApiResponse<?> updateMyMap(
      @Valid @RequestBody UpdateMyMapDto updateMyMapDto,
      @PathVariable Long myMapId
  ){
    MyMapResponse myMapResponse =
        myMapService.updateMyMap(updateMyMapDto, myMapId);
    return ApiResponse.createSuccessMessage(myMapResponse, MY_MAP_UPDATE_SUCCESS);
  }

  @DeleteMapping("/{myMapId}")
  public ApiResponse<?> deleteMyMap(@PathVariable Long myMapId){
    myMapService.deleteMyMap(myMapId);
    return ApiResponse.createSuccessMessage(true, DELETE_SUCCESS);
  }
}
