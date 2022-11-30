package com.example.edubox.model.res;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EGender;
import com.example.edubox.constant.ERoleType;
import com.example.edubox.entity.User;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MemberRes {
    private String fullName;
    private String email;
    private EGender gender;
    private Integer age;
    private String role;
    private String code;
    private ECommonStatus status;

    public static MemberRes valueOf(User user, ERoleType roleType) {
        MemberRes memberRes = MemberRes.builder()
        		.fullName(user.getFullName())
        		.email(user.getEmail())
        		.gender(user.getGender())
        		.age(user.getAge())
        		.role(roleType.getValue())
        		.code(user.getCode())
        		.status(user.getStatus())
        		.build();
        return memberRes;

    }
}
