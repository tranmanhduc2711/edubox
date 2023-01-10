package com.example.edubox.model.res;

import com.example.edubox.constant.EPresentType;
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
    private EPresentType type;
    private String groupName;
    private UserRes host;
    private int totalSlide;
    private Boolean isRunning;
    private LocalDateTime createdAt;

    public static PresentationRes valueOf(Presentation presentation) {
        return PresentationRes.builder()
                .code(presentation.getCode())
                .name(presentation.getName())
                .description(presentation.getDescription())
                .type(presentation.getPresentType())
                .groupName(presentation.getGroup().getGroupName())
                .host(UserRes.valueOf(presentation.getHost()))
                .totalSlide(presentation.getTotalSlide())
                .isRunning(presentation.getIsRunning())
                .createdAt(presentation.getCreatedAt())
                .build();

    }
}
