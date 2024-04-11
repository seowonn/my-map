package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.VisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

  Page<VisitLog> findAllByMyMapId(Long myMapId, Pageable pageable);
}
