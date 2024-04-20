package com.seowonn.mymap.service;

import static com.seowonn.mymap.type.ErrorCode.CATEGORY_NOT_FOUND;

import com.seowonn.mymap.dto.category.CategoryForm;
import com.seowonn.mymap.dto.category.CategoryResponse;
import com.seowonn.mymap.entity.Category;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public List<CategoryResponse> addCategory(CategoryForm categoryForm) {

    List<CategoryResponse> responses = new ArrayList<>();

    for (String category : categoryForm.getCategories()){
      Optional<Category> byCategoryName = categoryRepository.findByCategoryName(
          category);
      if(byCategoryName.isEmpty()){
        Category from = Category.from(category);
        categoryRepository.save(from);
        responses.add(CategoryResponse.from(from));
      }
    }

    return responses;
  }

  @Transactional
  public void deleteCategory(CategoryForm categoryForm) {

    for (String category : categoryForm.getCategories()){
      Category categoryToDelete =
          categoryRepository.findByCategoryName(category)
          .orElseThrow(() -> new MyMapSystemException(CATEGORY_NOT_FOUND));
      categoryRepository.delete(categoryToDelete);
    }

  }
}
