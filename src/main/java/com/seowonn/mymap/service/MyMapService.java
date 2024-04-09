package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.MyMap.NewMyMapDto;
import com.seowonn.mymap.dto.MyMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiDo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyMapService {

  Page<SiDo> getSiDoCites(Pageable pageable);

  MyMap registerNewMap(NewMyMapDto newMyMapDto);

  MyMap updateMyMap(UpdateMyMapDto updateMyMapDto, String userId);

}
