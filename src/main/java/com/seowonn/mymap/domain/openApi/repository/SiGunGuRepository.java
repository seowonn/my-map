package com.seowonn.mymap.domain.openApi.repository;

import com.seowonn.mymap.domain.openApi.entity.SiGunGu;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiGunGuRepository extends JpaRepository<SiGunGu, Long> {

  boolean existsBySiGunGuCode(String siGunGuCode);
  Page<SiGunGu> findBySiDoSiDoCode(String siDoCode, Pageable pageable);

  Optional<SiGunGu> findBySiGunGuCode(String siGunGuCode);
}
