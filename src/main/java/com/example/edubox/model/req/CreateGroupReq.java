package com.example.edubox.model.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGroupReq {
    private String name;
    private String description;
    private Integer capacity;
    private String ownerCode;
}
