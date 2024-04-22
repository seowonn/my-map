package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.ACCESS_DENIED;
import static com.seowonn.mymap.type.ErrorCode.NO_LIKES_CLICKED;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.VISIT_LOG_NOT_FOUND;

import com.seowonn.mymap.dto.BookMarkDto;
import com.seowonn.mymap.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.dto.visitLog.VisitLogUserInputForm;
import com.seowonn.mymap.entity.BookMarks;
import com.seowonn.mymap.entity.Likes;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.BookMarksRepository;
import com.seowonn.mymap.repository.LikesRepository;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.VisitLogRepository;
import com.seowonn.mymap.service.RedisLockService;
import com.seowonn.mymap.service.VisitorService;
import com.seowonn.mymap.type.Access;
import com.seowonn.mymap.type.Boolean;
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
  private final BookMarksRepository bookMarksRepository;

  private final RedisLockService redisLockService;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public VisitLogResponse getVisitLogDetails(Long visitLogId) {

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    String userId = authentication.getName();

    redisLockService.controlViewers(visitLogId, userId);

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    if(bookMarksRepository.existsByVisitLogId(visitLogId)){
      return VisitLogResponse.from(visitLog, Boolean.TRUE.getFlag());
    }
    return VisitLogResponse.from(visitLog, Boolean.FALSE.getFlag());
  }

  @Transactional
  public void addVisitLogLikes(Member member, VisitLog visitLog) {

    // 이미 좋아요를 누른 사람인지 확인
    if (likesRepository.findByVisitLogAndVisitId(visitLog, member.getId())
        .isPresent()) {
      return;
    }

    Likes likes = Likes.of(member.getId(), visitLog);
    visitLogRepository.addLikes(visitLog.getId());
    likesRepository.save(likes);

  }

  @Transactional
  public void deleteVisitLogLikes(Member member, VisitLog visitLog) {

    Likes likes = likesRepository.findByVisitLogAndVisitId(visitLog,
            member.getId())
        .orElseThrow(() -> new MyMapSystemException(NO_LIKES_CLICKED));

    likesRepository.delete(likes);
    if (visitLog.getLikes() > 0) {
      visitLogRepository.deleteLikes(visitLog.getId());
      entityManager.refresh(visitLog);  // VisitLog 엔티티 새로고침
    }
  }

  @Override
  public Page<VisitLogResponse> getAllVisitLogsFromMyMap(Long myMapId,
      Pageable pageable) {

    // 해당 마이맵의 공개 설정 유무로 1차 필터
    if (!myMapRepository.existsByIdAndAccess(myMapId, Access.PUBLIC)) {
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    Page<VisitLog> allPublicVisitLogs = visitLogRepository.findAllPublicVisitLogs(
        myMapId, Access.PUBLIC, pageable);

    return VisitLogResponse.fromPage(allPublicVisitLogs);
  }

  public void addUserBookMark(Member member, VisitLog visitLog, String userId) {

    Optional<BookMarks> byMemberUserId =
        bookMarksRepository.findByMemberUserIdAndVisitLog(userId, visitLog);

    if (byMemberUserId.isEmpty()) {
      BookMarks from = BookMarks.from(member, visitLog);
      bookMarksRepository.save(from);
    }
  }

  public void deleteUserBookMark(Member member, VisitLog visitLog,
      String userId) {

    Optional<BookMarks> byMemberUserId =
        bookMarksRepository.findByMemberUserIdAndVisitLog(userId, visitLog);

    if(byMemberUserId.isPresent()){
      BookMarks from = BookMarks.from(member, visitLog);
      bookMarksRepository.delete(from);
    }
  }

  @Override
  @Transactional
  public VisitLogResponse applyUserInput(Long myMapId, Long visitLogId,
      VisitLogUserInputForm form) {

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    String userId = authentication.getName();

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    VisitLog visitLog = visitLogRepository.findById(visitLogId)
        .orElseThrow(() -> new MyMapSystemException(VISIT_LOG_NOT_FOUND));

    if (form.getLiked().equals(Boolean.TRUE.getFlag())) {
      addVisitLogLikes(member, visitLog);
    } else {
      deleteVisitLogLikes(member, visitLog);
    }

    if (form.getMarked().equals(Boolean.TRUE.getFlag())) {
      addUserBookMark(member, visitLog, userId);
    } else {
      deleteUserBookMark(member, visitLog, userId);
    }

    entityManager.refresh(visitLog);
    return VisitLogResponse.from(visitLog, form.getMarked());
  }

  @Override
  public Page<BookMarkDto> getMarkedLogs(Pageable pageable) {

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    String userId = authentication.getName();

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    Page<BookMarks> allByMember = bookMarksRepository.findAllByMember(member,
        pageable);

    return BookMarkDto.fromPage(allByMember);
  }

  @Override
  public Page<BookMarkDto> getCategoryMarkedLogs(String categoryName,
      Pageable pageable) {

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    String userId = authentication.getName();

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    Page<BookMarks> allByMemberAndVisitLogCategoryCategoryName =
        bookMarksRepository.findAllByMemberAndVisitLogCategoryCategoryName(
        member, categoryName, pageable
    );

    return BookMarkDto.fromPage(allByMemberAndVisitLogCategoryCategoryName);
  }
}
