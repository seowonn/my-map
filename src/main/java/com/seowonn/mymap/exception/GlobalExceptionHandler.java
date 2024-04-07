package com.seowonn.mymap.exception;

import static com.seowonn.mymap.type.ErrorCode.INVALID_SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.type.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleMethodArgumentValidException(
      MethodArgumentNotValidException e) {

    List<String> errorMessages = e.getBindingResult().getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();

    log.error("[MethodArgumentValidException]");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createValidationFail(errorMessages));
  }


  @ExceptionHandler(MyMapSystemException.class)
  public ResponseEntity<ApiResponse<?>> handleMyMapSystemException (
      MyMapSystemException e) {
    log.error("[MyMapSystemException] : {}", e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createFail(e.getErrorCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleException (Exception e) {
    log.error("[InternalServerException] : {}", e.getMessage());
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(ApiResponse.createFail(INVALID_SERVER_ERROR));
  }
}
