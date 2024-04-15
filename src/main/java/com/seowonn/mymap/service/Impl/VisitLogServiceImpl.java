package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.ACCESS_DENIED;
import static com.seowonn.mymap.type.ErrorCode.FILES_EXCEED;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_REGION;
import static com.seowonn.mymap.type.ErrorCode.MY_MAP_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.NO_LIKES_CLICKED;
import static com.seowonn.mymap.type.ErrorCode.REGION_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.VISIT_LOG_NOT_FOUND;

import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.dto.UpdateVisitLogDto;
import com.seowonn.mymap.entity.Image;
import com.seowonn.mymap.entity.Likes;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.LikesRepository;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.repository.VisitLogRepository;
import com.seowonn.mymap.service.CheckService;
import com.seowonn.mymap.service.VisitLogService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitLogServiceImpl implements VisitLogService {

  private final VisitLogRepository visitLogRepository;
  private final MyMapRepository myMapRepository;
  private final SiGunGuRepository siGunGuRepository;
  private final MemberRepository memberRepository;
  private final LikesRepository likesRepository;

  private final S3ServiceImpl s3Service;
  private final CheckService checkService;
  private final MyMapServiceImpl myMapService;
  private final RedisServiceImpl redisService;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public VisitLog createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto){

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

    return visitLog;
  }

  @Override
  public Page<VisitLog> getVisitLogs(Long myMapId, Pageable pageable) {

    MyMap myMap = myMapRepository.findById(myMapId)
        .orElseThrow(() -> new MyMapSystemException(MY_MAP_NOT_FOUND));

    // 로그인한 사용자가 조회된 마이맵 작성자인지 확인
    checkService.checkIsLoginUser(myMap.getMember().getUserId());

    return visitLogRepository.findAllByMyMapId(myMapId, pageable);
  }

  @Override
  public void deleteVisitLog(Long myMapId, Long visitLogId) {

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    // 해당 마이맵의 방문일지인지 확인
    if (visitLog.getMyMap().getId() != myMapId){
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    List<Image> images = visitLog.getImages();
    for(Image image : images){
      s3Service.deleteS3File(image.getImageUrl());
    }

    visitLogRepository.delete(visitLog);
  }

  @Override
  @Transactional
  public VisitLog updateLog(Long myMapId, Long visitLogId,
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

    return visitLog;
  }

  @Override
  @Transactional
  public void updateViews(Long id) {
    visitLogRepository.updateViews(id);
  }

  @Override
  @Transactional
  public VisitLog visitLogDetails(Long visitLogId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    // redis에 해당 방문일지(id값)으로 조회되는 set이 있는지 검사
    Set<String> viewsData = redisService.getViewsData(visitLogId);

    if(viewsData.isEmpty()){ // set 존재 X, redis 데이터 생성, view +1
      redisService.makeViewCountExpire(visitLogId, userId);
      updateViews(visitLogId);
    } else { // set 존재
      if(!viewsData.contains(userId)){  // set.add가 true일 때만 view +1
        redisService.addViewCount(visitLogId, userId);
        updateViews(visitLogId);
      }
    }

    return visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));
  }

  @Override
  @Transactional
  public VisitLog addVisitLogLikes(Long myMapId, Long visitLogId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    // 이미 좋아요를 누른 사람인지 확인
    Optional<Likes> likesOptional =
        likesRepository.findByVisitLogAndVisitId(visitLog, member.getId());

    if(likesOptional.isEmpty()){
      Likes likes = Likes.of(member.getId(), visitLog);
      likesRepository.save(likes);
      visitLogRepository.addLikes(visitLogId);
      entityManager.refresh(visitLog);  // VisitLog 엔티티 새로고침
    }

    return visitLog;
  }

  @Override
  @Transactional
  public VisitLog deleteVisitLogLikes(Long myMapId, Long visitLogId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    Likes likes = likesRepository.findByVisitLogAndVisitId(visitLog,
            member.getId())
        .orElseThrow(() -> new MyMapSystemException(NO_LIKES_CLICKED));

    likesRepository.delete(likes);
    if(visitLog.getLikes() > 0){
      visitLogRepository.deleteLikes(visitLogId);
      entityManager.refresh(visitLog);  // VisitLog 엔티티 새로고침
    }
    return visitLog;
  }
}
