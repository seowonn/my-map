package com.seowonn.mymap.domain.member.entity;

import com.seowonn.mymap.domain.visitLogForWriter.entity.VisitLog;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Likes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private long visitId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "visitLog")
  private VisitLog visitLog;

  public static Likes of(long visitId, VisitLog visitLog){
    return Likes.builder()
        .visitId(visitId)
        .visitLog(visitLog)
        .build();
  }

}
