package com.example.edubox.controller.base;

import com.example.edubox.exception.BusinessException;
import com.example.edubox.support.web.ResponseSupport;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor
@ControllerAdvice
public class ExceptionHandlerAdvice {
    @Autowired
    ResponseSupport responseSupport;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handle(HttpServletRequest request, BusinessException e) {
//        LogData logData = baseLogData(request);
//        logData.append(LogData.LogKey.ERR_MSG, e.getMessage())
//                .append(LogData.LogKey.EX_MSG, e.getMessage())
//                .append(LogData.LogKey.ERR_CODE, e.getCode())
//                .append(LogData.LogKey.HTTP_STATUS, HttpStatus.OK);
//        Log.error(logData);

        return new ResponseEntity<>(responseSupport.errorResponse(e.getCode()), HttpStatus.OK);
    }
}
