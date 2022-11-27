package com.example.edubox.model.req;

import com.example.edubox.constant.ERoleType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleAssignmentReq {
    private String groupCode;
    private String ownerCode;
    private String userCode;
    private ERoleType roleType;
}
