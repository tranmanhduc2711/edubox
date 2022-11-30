package com.example.edubox.controller.base;

import com.example.edubox.constant.ErrorCode;
import com.example.edubox.support.web.BaseResponseData;
import com.example.edubox.support.web.Meta;
import com.example.edubox.support.web.ResponseSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

public abstract class BaseController {
    @Autowired
    protected ResponseSupport responseSupport;

    protected  ResponseEntity<?> success(Object data) {
        BaseResponseData responseData = new BaseResponseData(
                responseSupport.createMeta(ErrorCode.SUCCESS.getValue()), data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
