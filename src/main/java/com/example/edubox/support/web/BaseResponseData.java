package com.example.edubox.support.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseData implements Serializable {
    private static final long serialVersionUID = -3530459430193310667L;

    private Meta meta;

    private Object data;

    public BaseResponseData(Meta meta) {
        this.meta = meta;
    }

    public static BaseResponseData instance(Meta meta, Object data) {
       return  new BaseResponseData(meta,data);
    }
    public static BaseResponseData success() {
        return new BaseResponseData(Meta.success(), null);
    }

    public static BaseResponseData error(int code, String message) {
        Meta meta = Meta.instance(code, message);
        return new BaseResponseData(meta, null);
    }

    public static BaseResponseData error(int code) {
        Meta meta = Meta.instance(code, null);
        return new BaseResponseData(meta, null);
    }
}
