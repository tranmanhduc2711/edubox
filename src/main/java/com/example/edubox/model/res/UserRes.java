package com.example.edubox.model.res;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EGender;
import com.example.edubox.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserRes {
    private String fullName;
    private String email;
    private EGender gender;
    private Integer age;
    private String role;
    private String code;
    private ECommonStatus status;

    public static UserRes valueOf(User user) {
        if(user == null) return null;
        return UserRes.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.getGender())
                .age(user.getAge())
                .code(user.getCode())
                .status(user.getStatus())
                .build();
    }
}
