package com.seowonn.mymap.dto.category;

import com.seowonn.mymap.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

  private String categoryName;

  public static CategoryResponse from(Category category){
    return CategoryResponse.builder()
        .categoryName(category.getCategoryName())
        .build();
  }
}
