package com.example.edubox.model.req;

import com.example.edubox.constant.EPresentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePresentationReq {
    private String name;
    private String description;
    private EPresentType type;
    private String groupCode;
}
