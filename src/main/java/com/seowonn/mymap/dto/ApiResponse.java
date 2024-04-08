package com.seowonn.mymap.dto;

import static com.seowonn.mymap.type.ApiStatus.FAIL_STATUS;
import static com.seowonn.mymap.type.ApiStatus.SUCCESS_STATUS;

import com.seowonn.mymap.type.ApiStatus;
import com.seowonn.mymap.type.ErrorCode;
import com.seowonn.mymap.type.SuccessMessage;
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
