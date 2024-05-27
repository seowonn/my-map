package com.seowonn.mymap.domain.member.repository;

import com.seowonn.mymap.domain.member.entity.Likes;
import com.seowonn.mymap.domain.visitLogForWriter.entity.VisitLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  Optional<Likes> findByVisitLogAndVisitId(VisitLog visitLog, Long visitId);
}
