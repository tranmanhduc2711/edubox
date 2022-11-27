package com.example.edubox.support.web;

import org.springframework.stereotype.Component;

@Component
public class ResponseSupport {

    public Meta createMeta(int code) {
        return Meta.instance(code,null);
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
