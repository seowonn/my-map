package com.seowonn.mymap.exception;

import static com.seowonn.mymap.type.ErrorCode.INVALID_SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.seowonn.mymap.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Logger LOGGER = LoggerFactory.getLogger(
      GlobalExceptionHandler.class);

  @ExceptionHandler(MyMapSystemException.class)
  public ResponseEntity<ApiResponse<?>> handleMyMapSystemException (
      MyMapSystemException e) {
    LOGGER.error("[MyMapSystemException] : {}", e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createFail(e.getErrorCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleException (Exception e) {
    LOGGER.error("[InternalServerException] : {}", e.getMessage());
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(ApiResponse.createFail(INVALID_SERVER_ERROR));
  }
}
