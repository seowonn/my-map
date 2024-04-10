package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.INCORRECT_REGION;
import static com.seowonn.mymap.type.ErrorCode.MY_MAP_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.REGION_NOT_FOUND;

import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.repository.VisitLogRepository;
import com.seowonn.mymap.service.VisitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitLogServiceImpl implements VisitLogService {

  private final VisitLogRepository visitLogRepository;
  private final MyMapRepository myMapRepository;
  private final SiGunGuRepository siGunGuRepository;

  @Override
  public VisitLog createVisitLog(Long myMapId,
      NewVisitLogDto newVisitLogDto) {

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

    VisitLog visitLog = VisitLog.buildFromDto(newVisitLogDto);
    visitLog.setMyMap(myMap);
    visitLog.setSiGunGu(siGunGu);

    return visitLogRepository.save(visitLog);
  }
}
