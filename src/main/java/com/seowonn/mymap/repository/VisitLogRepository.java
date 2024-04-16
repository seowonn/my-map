package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.type.Access;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

  Page<VisitLog> findAllByMyMapId(Long myMapId, Pageable pageable);

  @Modifying
  @Query("update VisitLog v set v.views = v.views + 1 where v.id = :id")
  void updateViews(Long id);

  @Modifying
  @Query("update VisitLog v set v.likes = v.likes + 1 where v.id = :id")
  void addLikes(Long id);

  @Modifying
  @Query("update VisitLog v set v.likes = v.likes - 1 where v.id = :id")
  void deleteLikes(Long id);

  @Query("SELECT v FROM VisitLog v WHERE v.myMap.id = :id AND v.access = :access ORDER BY CASE WHEN v.recommendOrder IS NULL THEN 1 ELSE 0 END, v.recommendOrder ASC, v.createdAt DESC")
  Page<VisitLog> findVisitLogs(Long id, Access access, Pageable pageable);

}
