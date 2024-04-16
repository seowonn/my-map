package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.ACCESS_DENIED;
import static com.seowonn.mymap.type.ErrorCode.NO_LIKES_CLICKED;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.VISIT_LOG_NOT_FOUND;

import com.seowonn.mymap.dto.visitLog.VisitLogDto;
import com.seowonn.mymap.entity.Likes;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.LikesRepository;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.VisitLogRepository;
import com.seowonn.mymap.service.VisitorService;
import com.seowonn.mymap.type.Access;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorServiceImpl implements VisitorService {

  private final VisitLogRepository visitLogRepository;
  private final MemberRepository memberRepository;
  private final LikesRepository likesRepository;
  private final MyMapRepository myMapRepository;

  @PersistenceContext
  private EntityManager entityManager;

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

    if (likesOptional.isEmpty()) {
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

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    String userId = authentication.getName();

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    Likes likes = likesRepository.findByVisitLogAndVisitId(visitLog,
            member.getId())
        .orElseThrow(() -> new MyMapSystemException(NO_LIKES_CLICKED));

    likesRepository.delete(likes);
    if (visitLog.getLikes() > 0) {
      visitLogRepository.deleteLikes(visitLogId);
      entityManager.refresh(visitLog);  // VisitLog 엔티티 새로고침
    }
    return visitLog;
  }

  @Override
  public Page<VisitLogDto> getAllVisitLogsFromMyMap(Long myMapId,
      Pageable pageable) {

    // 해당 마이맵의 공개 설정 유무로 1차 필터
    boolean exists = myMapRepository.existsByIdAndAccess(myMapId, Access.PUBLIC);
    if (!exists) {
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    Page<VisitLog> allPublicVisitLogs = visitLogRepository.findVisitLogs(
        myMapId, Access.PUBLIC, pageable);

    return VisitLogDto.toDtoList(allPublicVisitLogs);
  }
}
