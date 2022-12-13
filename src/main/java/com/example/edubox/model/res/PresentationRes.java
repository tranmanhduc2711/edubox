package com.example.edubox.model.res;

import com.example.edubox.entity.Presentation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PresentationRes {
    private String code;
    private String name;
    private String description;
    private String type;
    private String groupName;
    private UserRes host;
    private int totalSlide;
    private LocalDateTime createdAt;

    public static PresentationRes valueOf(Presentation presentation) {
        return PresentationRes.builder()
                .code(presentation.getCode())
                .name(presentation.getName())
                .description(presentation.getDescription())
                .type(presentation.getPresentType().getValue())
                .groupName(presentation.getGroup().getGroupName())
                .host(UserRes.valueOf(presentation.getHost()))
                .totalSlide(presentation.getTotalSlide())
                .createdAt(presentation.getCreatedAt())
                .build();

    }
}
