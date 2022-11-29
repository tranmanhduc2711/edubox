package com.example.edubox.support.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseSupport {
    @Autowired
    Translator translator;

    public Meta createMeta(int code) {
        String message = translator.toLocale(code);
        return Meta.instance(code,message);
    }

    public BaseResponseData errorResponse(int code,String message) {
        Meta meta = Meta.instance(code, message);
        return new BaseResponseData(meta, null);
    }
    public BaseResponseData errorResponse(int code) {
        Meta meta = createMeta(code);
        return new BaseResponseData(meta, null);
    }
}
