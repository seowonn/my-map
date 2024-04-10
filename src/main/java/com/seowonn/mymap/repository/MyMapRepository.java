package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.MyMap;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyMapRepository extends JpaRepository<MyMap, Long> {

  @NonNull
  Optional<MyMap> findById(@NonNull Long id);

}
