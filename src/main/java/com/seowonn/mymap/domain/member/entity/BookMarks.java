package com.seowonn.mymap.domain.member.entity;

import com.seowonn.mymap.domain.visitLogForWriter.entity.VisitLog;
import com.seowonn.mymap.global.entity.BaseEntity;
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
public class BookMarks extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "visitLog")
  private VisitLog visitLog;

  public static BookMarks from (Member member, VisitLog visitLog){
    return BookMarks.builder()
        .member(member)
        .visitLog(visitLog)
        .build();
  }


}
