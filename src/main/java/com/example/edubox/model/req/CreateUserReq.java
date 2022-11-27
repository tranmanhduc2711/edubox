package com.example.edubox.model.req;

import com.example.edubox.constant.EGender;
import com.example.edubox.validation.Password;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Data
@Setter
@Getter
public class CreateUserReq {
    private String username;

    @Password
    private String password;

    @Email
    private String email;

    private String fullName;

    private EGender gender;

    private Integer age;
}
