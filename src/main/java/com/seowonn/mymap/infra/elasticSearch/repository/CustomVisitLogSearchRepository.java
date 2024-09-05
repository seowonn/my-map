package com.seowonn.mymap.infra.elasticSearch.repository;

import com.seowonn.mymap.infra.elasticSearch.document.VisitLogDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomVisitLogSearchRepository {

  Page<VisitLogDocument> findAllByPlaceNameOrContentPrefix(String place, Pageable pageable);

}
