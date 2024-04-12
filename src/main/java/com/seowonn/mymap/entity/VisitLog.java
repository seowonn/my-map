package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.dto.UpdateVisitLogDto;
import com.seowonn.mymap.type.IsPublic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VisitLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String placeName;

  @Column(length = 1000)
  private String content;

  @Column
  private long views;

  @Column
  private long likes;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private IsPublic isPublic;

  @Column
  private int recommendOrder;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "myMap")
  @JsonBackReference
  private MyMap myMap;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "siGunGu")
  @JsonBackReference
  private SiGunGu siGunGu;

  @OneToMany(mappedBy = "visitLog", fetch = FetchType.EAGER)
  @ToString.Exclude
  @Builder.Default
  @JsonManagedReference
  private List<Image> images = new ArrayList<>();

  public static VisitLog buildFromDto(
      NewVisitLogDto newVisitLogDto, MyMap myMap, SiGunGu siGunGu) {

    IsPublic isPublic = IsPublic.valueOf(newVisitLogDto.getIsPublic().toUpperCase());

    return VisitLog.builder()
        .placeName(newVisitLogDto.getPlaceName())
        .content(newVisitLogDto.getContent())
        .views(0)
        .likes(0)
        .isPublic(isPublic)
        .recommendOrder(newVisitLogDto.getRecommendOrder())
        .myMap(myMap)
        .siGunGu(siGunGu)
        .build();
  }

  public void updateVisitLog (UpdateVisitLogDto updateVisitLogDto){

    IsPublic isPublic = IsPublic.valueOf(updateVisitLogDto.getIsPublic().toUpperCase());

    this.placeName = updateVisitLogDto.getPlaceName();
    this.content = updateVisitLogDto.getContent();
    this.isPublic = isPublic;
    this.recommendOrder = updateVisitLogDto.getRecommendOrder();;
  }
}
