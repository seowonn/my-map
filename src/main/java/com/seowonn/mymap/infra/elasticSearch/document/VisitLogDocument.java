package com.seowonn.mymap.infra.elasticSearch.document;

import com.seowonn.mymap.domain.visitLogForWriter.entity.VisitLog;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "keyword")
public class VisitLogDocument {

  @Id
  private String id;

  private String city;

  @Field(type = FieldType.Text)
  private String placeName;

  @Field(type = FieldType.Text)
  private String content;

  private String category;

  public static VisitLogDocument from(VisitLog visitLog){
    return VisitLogDocument.builder()
        .city(visitLog.getSiGunGu().getSiGunGuName())
        .placeName(visitLog.getPlaceName())
        .content(visitLog.getContent())
        .category(visitLog.getCategory().getCategoryName())
        .build();
  }

}
