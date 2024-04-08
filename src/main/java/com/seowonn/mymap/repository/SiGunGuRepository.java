package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.SiGunGu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiGunGuRepository extends JpaRepository<SiGunGu, Long> {

  boolean existsBySiGunGuCode(String siGunGuCode);
}
