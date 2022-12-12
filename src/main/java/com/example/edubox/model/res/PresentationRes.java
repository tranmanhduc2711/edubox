package com.example.edubox.model.res;

import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PresentationRes {
    private String code;
    private String name;
    private String description;
    private String type;
    private String groupName;
    private UserRes hostName;

    public static PresentationRes valueOf(Presentation presentation) {
        return PresentationRes.builder()
                .code(presentation.getCode())
                .name(presentation.getName())
                .description(presentation.getDescription())
                .type(presentation.getPresentType().getValue())
                .groupName(presentation.getGroup().getGroupName())
                .hostName(UserRes.valueOf(presentation.getHost()))
                .build();

    }
}
