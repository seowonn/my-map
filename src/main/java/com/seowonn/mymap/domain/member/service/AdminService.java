package com.seowonn.mymap.domain.member.service;

import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.visitLog.dto.CategoryDto;
import com.seowonn.mymap.domain.visitLog.entity.Category;
import com.seowonn.mymap.domain.visitLog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.seowonn.mymap.global.type.ErrorCode.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public List<CategoryDto.CategoryResponse> addCategory(CategoryDto.CategoryRequest categoryRequest) {

    List<CategoryDto.CategoryResponse> responses = new ArrayList<>();

    for (String category : categoryRequest.getCategories()){
      Optional<Category> byCategoryName = categoryRepository.findByCategoryName(
          category);
      if(byCategoryName.isEmpty()){
        Category from = Category.from(category);
        categoryRepository.save(from);
        responses.add(CategoryDto.CategoryResponse.from(from));
      }
    }

    return responses;
  }

  @Transactional
  public void deleteCategory(CategoryDto.CategoryRequest categoryRequest) {

    for (String category : categoryRequest.getCategories()){
      Category categoryToDelete =
          categoryRepository.findByCategoryName(category)
          .orElseThrow(() -> new MyMapSystemException(CATEGORY_NOT_FOUND));
      categoryRepository.delete(categoryToDelete);
    }

  }
}
