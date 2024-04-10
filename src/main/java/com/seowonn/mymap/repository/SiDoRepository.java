package com.seowonn.mymap.repository;

import com.seowonn.mymap.entity.SiDo;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiDoRepository extends JpaRepository<SiDo, Long> {
  boolean existsBySiDoCode(String siDoCode);

  Optional<SiDo> findBySiDoName(String siDoName);

  @NonNull
  Page<SiDo> findAll(@NonNull Pageable pageable);

  Optional<SiDo> findBySiDoCode(String siDoCode);
}
