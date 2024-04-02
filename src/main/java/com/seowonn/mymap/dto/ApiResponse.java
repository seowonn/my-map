package com.seowonn.mymap.dto;

import com.seowonn.mymap.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

  private static final String SUCCESS_STATUS = "success";
  private static final String FAIL_STATUS = "fail";

  private String status;
  private T data;
  private String message;

  public static <T> ApiResponse<?> createSuccess(T data){
    return ApiResponse.builder()
        .status(SUCCESS_STATUS)
        .data(data)
        .build();
  }

  public static ApiResponse<?> createFail(ErrorCode errorCode){
    return ApiResponse.builder()
        .status(FAIL_STATUS)
        .message(errorCode.getDescription())
        .build();
  }

}
