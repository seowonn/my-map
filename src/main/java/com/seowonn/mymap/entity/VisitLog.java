package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.dto.UpdateVisitLogDto;
import com.seowonn.mymap.type.Access;
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
  private int recommendOrder;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "myMap")
  @JsonBackReference
  private MyMap myMap;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "siGunGu")
  @JsonBackReference
  private SiGunGu siGunGu;

  @OneToMany(mappedBy = "visitLog", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  @JsonManagedReference
  private List<Image> images = new ArrayList<>();

  public static VisitLog ofNewVisitLogAndMyMapAndSiGunGu(
      NewVisitLogDto newVisitLogDto, MyMap myMap, SiGunGu siGunGu) {

    Access access = Access.valueOf(
        newVisitLogDto.getAccess().toUpperCase());

    return VisitLog.builder()
        .placeName(newVisitLogDto.getPlaceName())
        .content(newVisitLogDto.getContent())
        .views(0)
        .likes(0)
        .access(access)
        .recommendOrder(newVisitLogDto.getRecommendOrder())
        .myMap(myMap)
        .siGunGu(siGunGu)
        .build();
  }

  public void updateVisitLog(UpdateVisitLogDto updateVisitLogDto) {

    Access access = Access.valueOf(
        updateVisitLogDto.getAccess().toUpperCase());

    this.placeName = updateVisitLogDto.getPlaceName();
    this.content = updateVisitLogDto.getContent();
    this.access = access;
    this.recommendOrder = updateVisitLogDto.getRecommendOrder();
    ;
  }
}
