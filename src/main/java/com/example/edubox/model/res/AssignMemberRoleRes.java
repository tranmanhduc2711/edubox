package com.example.edubox.model.res;

import com.example.edubox.constant.ERoleType;
import lombok.Data;

@Data
public class AssignMemberRoleRes {
    private  String memberCode;
    private  String fullName;
    private  String username;
    private ERoleType role;
}
