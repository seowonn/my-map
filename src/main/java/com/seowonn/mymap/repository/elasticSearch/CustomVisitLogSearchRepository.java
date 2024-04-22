package com.seowonn.mymap.repository.elasticSearch;

import com.seowonn.mymap.entity.elasticDB.VisitLogDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomVisitLogSearchRepository {

  Page<VisitLogDocument> findAllByPlaceNameOrContentPrefix(String place, Pageable pageable);

}
