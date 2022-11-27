package com.example.edubox.support.web;

import com.example.edubox.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meta implements Serializable {
    private static final long serialVersionUID = -8561759068281851154L;
    private int code;
    private String message;

    public static Meta instance(int code,String message) {
        return new Meta(code,message);
    }
    public static Meta success() {
        return new Meta(ErrorCode.SUCCESS.getValue(),ErrorCode.SUCCESS.getName());
    }
}
