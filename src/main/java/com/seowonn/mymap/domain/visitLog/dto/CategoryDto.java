package com.seowonn.mymap.domain.visitLog.dto;

import com.seowonn.mymap.domain.visitLog.entity.Category;
import lombok.*;

import java.util.List;

public class CategoryDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryRequest {

        private List<String> categories;

    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse {

        private String categoryName;

        public static CategoryResponse from(Category category){
            return CategoryResponse.builder()
                    .categoryName(category.getCategoryName())
                    .build();
        }
    }
}
