package com.seowonn.mymap.service.Impl;

import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoResponse;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuResponse;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.repository.SiDoRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

  private final SiDoRepository siDoRepository;
  private final SiGunGuRepository siGunGuRepository;

  @Override
  public Page<SiDoResponse> getSiDoCites(Pageable pageable) {
    Page<SiDo> siDos = siDoRepository.findAll(pageable);
    return SiDoResponse.fromPage(siDos);
  }

  @Override
  public Page<SiGunGuResponse> getSiGunGuCites(String siDoCode, Pageable pageable) {
    Page<SiGunGu> siGunGus =
        siGunGuRepository.findBySiDoSiDoCode(siDoCode, pageable);
    return SiGunGuResponse.fromPage(siGunGus);
  }

}
