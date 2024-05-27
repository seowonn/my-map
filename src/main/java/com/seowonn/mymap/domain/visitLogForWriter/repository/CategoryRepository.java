package com.seowonn.mymap.domain.visitLogForWriter.repository;

import com.seowonn.mymap.domain.visitLogForWriter.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByCategoryName(String categoryName);
}
