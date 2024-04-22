package com.seowonn.mymap.dto.myMap;

import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.type.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@Builder
public class MyMapResponse {

  private String myMapTitle;
  private Access access;
  private String userName;
  private String siDoName;
  private long visitLogCnt;

  public static MyMapResponse from(MyMap myMap){
    return MyMapResponse.builder()
        .myMapTitle(myMap.getMyMapTitle())
        .access(myMap.getAccess())
        .userName(myMap.getMember().getUserName())
        .siDoName(myMap.getSiDo().getSiDoName())
        .visitLogCnt(myMap.getVisitLogs().size())
        .build();
  }

  public static Page<MyMapResponse> fromPage(Page<MyMap> myMapPage){
    return myMapPage.map(myMap -> MyMapResponse.builder()
        .myMapTitle(myMap.getMyMapTitle())
        .access(myMap.getAccess())
        .userName(myMap.getMember().getUserName())
        .siDoName(myMap.getSiDo().getSiDoName())
        .visitLogCnt(myMap.getVisitLogs().size())
        .build());
  }

}
