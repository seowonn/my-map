package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoResponse;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuResponse;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.entity.elasticDB.VisitLogDocument;
import com.seowonn.mymap.repository.SiDoRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.repository.elasticSearch.VisitLogSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final SiDoRepository siDoRepository;
  private final SiGunGuRepository siGunGuRepository;
  private final ElasticsearchOperations elasticsearchOperations;
  private final VisitLogSearchRepository visitLogSearchRepository;

  public void save(VisitLog visitLog){
    elasticsearchOperations.save(VisitLogDocument.from(visitLog));
  }

  public Page<SiDoResponse> getSiDoCites(Pageable pageable) {
    Page<SiDo> siDos = siDoRepository.findAll(pageable);
    return SiDoResponse.fromPage(siDos);
  }

  public Page<SiGunGuResponse> getSiGunGuCites(String siDoCode, Pageable pageable) {
    Page<SiGunGu> siGunGus =
        siGunGuRepository.findBySiDoSiDoCode(siDoCode, pageable);
    return SiGunGuResponse.fromPage(siGunGus);
  }

  public Page<VisitLogDocument> searchVisitLogByKeyword(String keyword, Pageable pageable) {
    return visitLogSearchRepository.findAllByPlaceNameOrContentPrefix(keyword, pageable);
  }
}
