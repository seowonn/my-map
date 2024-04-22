package com.seowonn.mymap.repository.elasticSearch;

import com.seowonn.mymap.entity.elasticDB.VisitLogDocument;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomVisitLogSearchRepositoryImpl implements CustomVisitLogSearchRepository {

  private final ElasticsearchOperations elasticsearchOperations;

  @Override
  public Page<VisitLogDocument> findAllByPlaceNameOrContentPrefix(String keyword,
      Pageable pageable) {

    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    boolQueryBuilder.should(QueryBuilders.prefixQuery("placeName", keyword));
    boolQueryBuilder.should(QueryBuilders.prefixQuery("content", keyword));


    Query searchQuery = new NativeSearchQueryBuilder()
        .withQuery(boolQueryBuilder)
        .withPageable(pageable)
        .build();

    SearchHits<VisitLogDocument> searchHits =
        elasticsearchOperations.search(searchQuery, VisitLogDocument.class);

    return new PageImpl<>(searchHits.getSearchHits().stream()
        .map(SearchHit::getContent)
        .collect(Collectors.toList()));
  }
}
