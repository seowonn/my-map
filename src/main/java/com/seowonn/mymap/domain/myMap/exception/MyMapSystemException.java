package com.seowonn.mymap.domain.myMap.exception;

import com.seowonn.mymap.global.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyMapSystemException extends RuntimeException{

    private ErrorCode errorCode;
    private String errorMessage;

    public MyMapSystemException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
