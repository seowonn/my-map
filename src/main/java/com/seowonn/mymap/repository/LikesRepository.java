package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.Likes;
import com.seowonn.mymap.entity.VisitLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  Optional<Likes> findByVisitLogAndVisitId(VisitLog visitLog, Long visitId);
}
