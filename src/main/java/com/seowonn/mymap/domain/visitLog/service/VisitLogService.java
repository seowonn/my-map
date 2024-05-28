package com.seowonn.mymap.domain.visitLog.service;

import static com.seowonn.mymap.global.type.ErrorCode.ACCESS_DENIED;
import static com.seowonn.mymap.global.type.ErrorCode.CATEGORY_NOT_FOUND;
import static com.seowonn.mymap.global.type.ErrorCode.FILES_EXCEED;
import static com.seowonn.mymap.global.type.ErrorCode.INCORRECT_REGION;
import static com.seowonn.mymap.global.type.ErrorCode.MY_MAP_NOT_FOUND;
import static com.seowonn.mymap.global.type.ErrorCode.REGION_NOT_FOUND;
import static com.seowonn.mymap.global.type.ErrorCode.VISIT_LOG_NOT_FOUND;

import com.seowonn.mymap.domain.myMap.service.MyMapService;
import com.seowonn.mymap.domain.visitLog.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.domain.visitLog.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.domain.visitLog.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.domain.visitLog.entity.Category;
import com.seowonn.mymap.domain.visitLog.entity.Image;
import com.seowonn.mymap.domain.myMap.entity.MyMap;
import com.seowonn.mymap.domain.openApi.entity.SiGunGu;
import com.seowonn.mymap.domain.visitLog.entity.VisitLog;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.visitLog.repository.CategoryRepository;
import com.seowonn.mymap.domain.myMap.repository.MyMapRepository;
import com.seowonn.mymap.domain.openApi.repository.SiGunGuRepository;
import com.seowonn.mymap.domain.visitLog.repository.VisitLogRepository;
import com.seowonn.mymap.infra.awsS3.service.S3Service;
import com.seowonn.mymap.infra.elasticSearch.service.SearchService;
import com.seowonn.mymap.infra.email.service.CheckService;
import com.seowonn.mymap.global.type.Boolean;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitLogService {

  private final VisitLogRepository visitLogRepository;
  private final MyMapRepository myMapRepository;
  private final SiGunGuRepository siGunGuRepository;
  private final CategoryRepository categoryRepository;

  private final S3Service s3Service;
  private final CheckService checkService;
  private final MyMapService myMapService;
  private final SearchService searchService;

  @Transactional
  public VisitLogResponse createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto){

    // 파일 개수 확인
    if(newVisitLogDto.getFiles().size() > 10){
      throw new MyMapSystemException(FILES_EXCEED);
    }

    // 마이맵 존재 확인
    MyMap myMap = myMapRepository.findById(myMapId)
        .orElseThrow(() -> new MyMapSystemException(MY_MAP_NOT_FOUND));

    // 시군구 정보 확인
    SiGunGu siGunGu = siGunGuRepository.findBySiGunGuCode(
            newVisitLogDto.getSiGunGuCode())
        .orElseThrow(() -> new MyMapSystemException(REGION_NOT_FOUND));

    // 마이맵에 속하는 지역인지 확인
    if(myMap.getSiDo() != siGunGu.getSiDo()){
      throw new MyMapSystemException(INCORRECT_REGION);
    }

    Category category =
        categoryRepository.findByCategoryName(newVisitLogDto.getCategory())
            .orElseThrow(() -> new MyMapSystemException(CATEGORY_NOT_FOUND));

    VisitLog visitLog = VisitLog.of(newVisitLogDto, myMap, siGunGu, category);
    visitLogRepository.save(VisitLog.setCategory(visitLog, category));
    // 파일 S3 업로드 수행
    s3Service.upload(newVisitLogDto.getFiles(), myMap, visitLog);

    searchService.save(visitLog);

    return VisitLogResponse.from(visitLog, Boolean.FALSE.getFlag());
  }

  public Page<VisitLogResponse> getUsersVisitLogs(Long myMapId, Pageable pageable) {

    MyMap myMap = myMapRepository.findById(myMapId)
        .orElseThrow(() -> new MyMapSystemException(MY_MAP_NOT_FOUND));

    // 로그인한 사용자가 조회된 마이맵 작성자인지 확인
    checkService.checkIsLoginUser(myMap.getMember().getUserId());

    // 방문자 보기란과 다른점 : Access.PRIVATE도 볼 수 있음
    Page<VisitLog> allByMyMapId =
        visitLogRepository.findAllByMyMapId(myMapId, pageable);

    return VisitLogResponse.fromPage(allByMyMapId);
  }

  public List<String> deleteVisitLogGetDeleteUrls(Long myMapId, Long visitLogId) {

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    // 해당 마이맵의 방문일지인지 확인
    if (visitLog.getMyMap().getId() != myMapId){
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    List<String> imageUrlsToDelete = new ArrayList<>();
    List<Image> images = visitLog.getImages();
    for(Image image : images){
      imageUrlsToDelete.add(image.getImageUrl());
    }

    visitLogRepository.delete(visitLog);
    return imageUrlsToDelete;
  }

  @Transactional
  public VisitLogResponse updateLog(Long myMapId, Long visitLogId,
      UpdateVisitLogDto updateVisitLogDto) {

    MyMap myMap = myMapService.checkMyMapUser(myMapId);

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    // 해당 마이맵의 방문일지인지 확인
    if (visitLog.getMyMap() != myMap){
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    Category category = categoryRepository.findByCategoryName(
            updateVisitLogDto.getCategory())
        .orElseThrow(() -> new MyMapSystemException(CATEGORY_NOT_FOUND));

    visitLog.updateVisitLog(updateVisitLogDto, category);

    if(updateVisitLogDto.getDeleteFileUrls() != null){
      for(String deleteUrl : updateVisitLogDto.getDeleteFileUrls()){
        s3Service.deleteVisitLogFile(deleteUrl, visitLog);
      }
    }

    return VisitLogResponse.from(visitLog, null);
  }

}
