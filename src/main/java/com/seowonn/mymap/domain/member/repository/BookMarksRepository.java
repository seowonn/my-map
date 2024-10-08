package com.seowonn.mymap.domain.member.repository;

import com.seowonn.mymap.domain.member.entity.BookMarks;
import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.visitLog.entity.VisitLog;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarksRepository extends JpaRepository<BookMarks, Long> {

  Page<BookMarks> findAllByMember(Member member, Pageable pageable);

  boolean existsByVisitLogId(Long visitLogId);

  Optional<BookMarks> findByMemberUserIdAndVisitLog(String userId, VisitLog visitLog);

  Page<BookMarks>findAllByMemberAndVisitLogCategoryCategoryName(
      Member member, String categoryName, Pageable pageable);
}
