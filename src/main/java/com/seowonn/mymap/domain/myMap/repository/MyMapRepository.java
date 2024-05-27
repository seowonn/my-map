package com.seowonn.mymap.domain.myMap.repository;

import com.seowonn.mymap.domain.myMap.entity.MyMap;
import com.seowonn.mymap.type.Access;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyMapRepository extends JpaRepository<MyMap, Long> {

  @NonNull
  Optional<MyMap> findById(@NonNull Long id);

  Page<MyMap> findAllByMemberUserIdOrderByCreatedAt(String userId, Pageable pageable);

  boolean existsByIdAndAccess(Long id, Access access);
}
