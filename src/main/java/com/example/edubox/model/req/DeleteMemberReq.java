package com.example.edubox.model.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteMemberReq {
    private String email;
    private String groupCode;
}
