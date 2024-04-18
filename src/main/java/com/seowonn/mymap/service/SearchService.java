package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoResponse;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
  Page<SiDoResponse> getSiDoCites(Pageable pageable);

  Page<SiGunGuResponse> getSiGunGuCites(String siDoId, Pageable pageable);
}
