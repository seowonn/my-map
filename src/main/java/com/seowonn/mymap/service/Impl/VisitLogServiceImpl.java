package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.ACCESS_DENIED;
import static com.seowonn.mymap.type.ErrorCode.FILES_EXCEED;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_REGION;
import static com.seowonn.mymap.type.ErrorCode.MY_MAP_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.REGION_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.VISIT_LOG_NOT_FOUND;

import com.seowonn.mymap.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.entity.Image;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.repository.VisitLogRepository;
import com.seowonn.mymap.service.CheckService;
import com.seowonn.mymap.service.VisitLogService;
import com.seowonn.mymap.type.Boolean;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitLogServiceImpl implements VisitLogService {

  private final VisitLogRepository visitLogRepository;
  private final MyMapRepository myMapRepository;
  private final SiGunGuRepository siGunGuRepository;

  private final S3ServiceImpl s3Service;
  private final CheckService checkService;
  private final MyMapServiceImpl myMapService;

  @Override
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

    VisitLog visitLog = VisitLog.ofNewVisitLogAndMyMapAndSiGunGu(newVisitLogDto, myMap, siGunGu);

    visitLogRepository.save(visitLog);

    // 파일 S3 업로드 수행
    s3Service.upload(newVisitLogDto.getFiles(), myMap, visitLog);

    return VisitLogResponse.from(visitLog, Boolean.FALSE.getFlag());
  }

  @Override
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

  @Override
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

  @Override
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

    visitLog.updateVisitLog(updateVisitLogDto);

    if(updateVisitLogDto.getDeleteFileUrls() != null){
      for(String deleteUrl : updateVisitLogDto.getDeleteFileUrls()){
        s3Service.deleteVisitLogFile(deleteUrl, visitLog);
      }
    }

    return VisitLogResponse.from(visitLog, null);
  }

}
