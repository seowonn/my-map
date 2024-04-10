package com.seowonn.mymap.service.Impl;

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
  public Page<SiDo> getSiDoCites(Pageable pageable) {
    return siDoRepository.findAll(pageable);
  }

  @Override
  public Page<SiGunGu> getSiGunGuCites(String siDoCode, Pageable pageable) {
    return siGunGuRepository.findBySiDoSiDoCode(siDoCode, pageable);
  }

}
