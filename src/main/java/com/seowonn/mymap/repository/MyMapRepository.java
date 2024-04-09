package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.MyMap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyMapRepository extends JpaRepository<MyMap, Long> {

  Optional<MyMap> findByMemberUserId(String userId);
}
