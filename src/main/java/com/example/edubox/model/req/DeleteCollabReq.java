package com.example.edubox.model.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteCollabReq {
    private String email;
    private String presentCode;
}
