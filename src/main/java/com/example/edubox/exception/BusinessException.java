package com.example.edubox.exception;

import com.example.edubox.constant.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1445048812692858310L;

    private final ErrorCode code;

    public BusinessException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return this.code.getValue();
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
