package com.seowonn.mymap.exception;

import com.seowonn.mymap.type.ErrorCode;
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
}
