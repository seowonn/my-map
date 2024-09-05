package com.seowonn.mymap.global.dto;

import static com.seowonn.mymap.global.type.ApiStatus.FAIL_STATUS;
import static com.seowonn.mymap.global.type.ApiStatus.SUCCESS_STATUS;

import com.seowonn.mymap.global.type.ErrorCode;
import com.seowonn.mymap.global.type.SuccessMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

  private String status;
  private T data;
  private String message;
  private List<String> messages;

  public static <T> ApiResponse<?> createSuccessMessage(T data, SuccessMessage message){
    return ApiResponse.builder()
        .status(SUCCESS_STATUS.getStatus())
        .data(data)
        .message(message.getDescription())
        .build();
  }

  public static <T> ApiResponse<?> createSuccess(T data){
    return ApiResponse.builder()
        .status(SUCCESS_STATUS.getStatus())
        .data(data)
        .build();
  }

  public static ApiResponse<?> createFail(ErrorCode errorCode){
    return ApiResponse.builder()
        .status(FAIL_STATUS.getStatus())
        .message(errorCode.getDescription())
        .build();
  }

  public static ApiResponse<?> createValidationFail(List<String> errorMessage){
    return ApiResponse.builder()
        .status(FAIL_STATUS.getStatus())
        .messages(errorMessage)
        .build();
  }

}
