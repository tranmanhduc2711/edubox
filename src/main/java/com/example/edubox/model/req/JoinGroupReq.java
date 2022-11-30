package com.example.edubox.model.req;

import com.example.edubox.constant.ERoleType;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinGroupReq {
    private String groupCode;
    private String email;
    private ERoleType roleType;
}
