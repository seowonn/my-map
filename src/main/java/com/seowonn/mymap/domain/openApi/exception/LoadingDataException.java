package com.seowonn.mymap.domain.openApi.exception;

import com.seowonn.mymap.global.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoadingDataException extends RuntimeException{

    private ErrorCode errorCode;
    private String errorMessage;

    public LoadingDataException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
