package com.seowonn.mymap.domain.visitLog.repository;

import com.seowonn.mymap.domain.visitLog.entity.Image;
import com.seowonn.mymap.domain.visitLog.entity.VisitLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  Optional<Image> findFirstByImageUrlAndVisitLog(String url, VisitLog visitLog);

}
