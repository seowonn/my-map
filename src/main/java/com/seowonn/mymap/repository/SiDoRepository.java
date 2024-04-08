package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.SiDo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiDoRepository extends JpaRepository<SiDo, Long> {
  boolean existsBySiDoCode(String siDoCode);

  Optional<SiDo> findBySiDoName(String siDoName);
}
