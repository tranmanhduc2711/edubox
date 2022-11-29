package com.example.edubox.model.req;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EGender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UpdateUserReq {
    @NotNull(message = "user code not null")
    private String code;

    private String fullName;

    private String password;

    private EGender gender;

    private Integer age;

    private String role;

    private ECommonStatus status;
}
