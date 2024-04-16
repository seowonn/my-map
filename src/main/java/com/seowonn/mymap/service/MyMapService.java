package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.myMap.NewMyMapDto;
import com.seowonn.mymap.dto.myMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.MyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyMapService {

  MyMap registerNewMap(NewMyMapDto newMyMapDto);

  Page<MyMap> getAllMyMaps(String userId, Pageable pageable);

  MyMap updateMyMap(UpdateMyMapDto updateMyMapDto, Long myMapId);

  void deleteMyMap(Long myMapId);

  MyMap checkMyMapUser(Long myMapId);

}
