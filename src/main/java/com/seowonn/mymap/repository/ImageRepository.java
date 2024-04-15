package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.Image;
import com.seowonn.mymap.entity.VisitLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  Optional<Image> findFirstByImageUrlAndVisitLog(String url, VisitLog visitLog);

}
