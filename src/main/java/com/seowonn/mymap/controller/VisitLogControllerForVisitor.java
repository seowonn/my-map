package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.VISIT_LOG_UPDATE_SUCCESS;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.service.Impl.VisitorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logs") // 마이맵 선택 후 작성
public class VisitLogControllerForVisitor {

  private final VisitorServiceImpl visitorService;

  @PostMapping("/likes/{myMapId}/{visitLogId}")
  public ApiResponse<?> addLikes(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId){
    VisitLog visitLog = visitorService.addVisitLogLikes(myMapId, visitLogId);
    return ApiResponse.createSuccessMessage(visitLog, VISIT_LOG_UPDATE_SUCCESS);
  }

  @DeleteMapping("/likes/{myMapId}/{visitLogId}")
  public ApiResponse<?> deleteLikes(
      @PathVariable Long myMapId,
      @PathVariable Long visitLogId){
    VisitLog visitLog = visitorService.deleteVisitLogLikes(myMapId, visitLogId);
    return ApiResponse.createSuccessMessage(visitLog, VISIT_LOG_UPDATE_SUCCESS);
  }

}
