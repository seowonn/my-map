package com.seowonn.mymap.infra.elasticSearch.repository;

import com.seowonn.mymap.infra.elasticSearch.document.VisitLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogSearchRepository extends
    ElasticsearchRepository<VisitLogDocument, Long>, CustomVisitLogSearchRepository {

}