package com.seowonn.mymap.domain.visitLog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seowonn.mymap.domain.member.entity.Likes;
import com.seowonn.mymap.domain.myMap.entity.MyMap;
import com.seowonn.mymap.domain.openApi.entity.SiGunGu;
import com.seowonn.mymap.domain.visitLog.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.domain.visitLog.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.global.entity.BaseEntity;
import com.seowonn.mymap.domain.member.type.Access;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitLog extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String placeName;

  @Column(length = 1000)
  private String content;

  @Column(columnDefinition = "int default 0", nullable = false)
  private int views;

  @Column(columnDefinition = "int default 0", nullable = false)
  private int likes;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Access access;

  @Column
  private Integer recommendOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "myMap")
  private MyMap myMap;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "siGunGu")
  private SiGunGu siGunGu;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category")
  private Category category;

  @OneToMany(mappedBy = "visitLog", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  private List<Image> images = new ArrayList<>();

  @OneToMany(mappedBy = "visitLog", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  @JsonIgnore
  private List<Likes> likesList = new ArrayList<>();

  public static VisitLog of(
      NewVisitLogDto newVisitLogDto, MyMap myMap, SiGunGu siGunGu,
      Category category) {

    Access access = Access.valueOf(
        newVisitLogDto.getAccess().toUpperCase());

    return VisitLog.builder()
        .placeName(newVisitLogDto.getPlaceName())
        .content(newVisitLogDto.getContent())
        .category(category)
        .views(0)
        .likes(0)
        .access(access)
        .recommendOrder(newVisitLogDto.getRecommendOrder())
        .myMap(myMap)
        .siGunGu(siGunGu)
        .build();
  }

  public static VisitLog setCategory(VisitLog visitLog, Category category){
    visitLog.category = category;
    return visitLog;
  }

  public void updateVisitLog(UpdateVisitLogDto updateVisitLogDto,
      Category category) {

    Access access = Access.valueOf(
        updateVisitLogDto.getAccess().toUpperCase());

    this.placeName = updateVisitLogDto.getPlaceName();
    this.content = updateVisitLogDto.getContent();
    this.access = access;
    this.recommendOrder = updateVisitLogDto.getRecommendOrder();
    this.category = category;
  }
}
