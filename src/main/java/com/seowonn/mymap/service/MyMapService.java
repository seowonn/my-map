package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.myMap.MyMapResponse;
import com.seowonn.mymap.dto.myMap.NewMyMapDto;
import com.seowonn.mymap.dto.myMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.MyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyMapService {

  MyMapResponse registerNewMap(NewMyMapDto newMyMapDto);

  Page<MyMapResponse> getAllMyMaps(String userId, Pageable pageable);

  MyMapResponse updateMyMap(UpdateMyMapDto updateMyMapDto, Long myMapId);

  void deleteMyMap(Long myMapId);

  MyMap checkMyMapUser(Long myMapId);

}
