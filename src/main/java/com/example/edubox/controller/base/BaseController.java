package com.example.edubox.controller.base;

import com.example.edubox.constant.ErrorCode;
import com.example.edubox.support.web.BaseResponseData;
import com.example.edubox.support.web.Meta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    public static ResponseEntity<?> success(Object data) {
        BaseResponseData responseData = new BaseResponseData(Meta.instance(ErrorCode.SUCCESS.getValue(), null), data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
