package com.seowonn.mymap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seowonn.mymap.dto.myMap.NewMyMapDto;
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
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MyMap extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String myMapTitle;

  @Column
  private long totalLikes;

  @Column(columnDefinition = "long default 0")
  private long totalViews;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Access access;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member")
  @JsonBackReference
  private Member member;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "siDo")
  private SiDo siDo;

  @OneToMany(mappedBy = "myMap", fetch = FetchType.EAGER,
      cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  @JsonManagedReference
  private List<VisitLog> visitLogs = new ArrayList<>();

  public static MyMap from(NewMyMapDto newMyMapDto) {

    Access access = Access.valueOf(newMyMapDto.getAccess().toUpperCase());

    return MyMap.builder()
        .myMapTitle(newMyMapDto.getMyMapTitle())
        .totalLikes(0)
        .totalViews(0)
        .access(access)
        .build();
  }


}
