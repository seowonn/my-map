package com.seowonn.mymap.infra.awsS3.exception;

import com.seowonn.mymap.global.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AWSS3Exception extends RuntimeException{

    private ErrorCode errorCode;
    private String errorMessage;

    public AWSS3Exception(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

  public AWSS3Exception(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
