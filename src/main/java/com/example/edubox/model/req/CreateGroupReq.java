package com.example.edubox.model.req;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CreateGroupReq implements Serializable {
    private static final long serialVersionUID = -6998206970817884631L;
    private String name;
    private String description;
    private Integer capacity;
}
