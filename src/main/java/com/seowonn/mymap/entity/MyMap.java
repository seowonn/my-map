package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seowonn.mymap.dto.myMap.NewMyMapDto;
import com.seowonn.mymap.type.IsPublic;
import jakarta.persistence.CascadeType;
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
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MyMap {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String myMapTitle;

  @Column
  private long totalLikes;

  @Column
  private long totalViews;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private IsPublic isPublic;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member")
  @JsonBackReference
  private Member member;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "siDo")
  private SiDo siDo;

  @OneToMany(mappedBy = "myMap", fetch = FetchType.EAGER,
      cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @ToString.Exclude
  @Builder.Default
  @JsonManagedReference
  private List<VisitationLog> visitationLogs = new ArrayList<>();

  public static MyMap buildFromDto(
      NewMyMapDto newMyMapDto) {

    IsPublic isPublic = IsPublic.valueOf(newMyMapDto.getIsPublic().toUpperCase());

    return MyMap.builder()
        .myMapTitle(newMyMapDto.getMyMapTitle())
        .totalLikes(0)
        .totalViews(0)
        .isPublic(isPublic)
        .build();
  }


}
