package com.seowonn.mymap.service;

import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.entity.SiGunGu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
  Page<SiDo> getSiDoCites(Pageable pageable);

  Page<SiGunGu> getSiGunGuCites(String siDoId, Pageable pageable);
}
