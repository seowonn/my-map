package com.seowonn.mymap.repository.elasticSearch;

import com.seowonn.mymap.entity.elasticDB.VisitLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogSearchRepository extends
    ElasticsearchRepository<VisitLogDocument, Long>, CustomVisitLogSearchRepository {

}