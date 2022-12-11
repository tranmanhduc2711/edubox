package com.example.edubox.model.req;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EPresentType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdatePresentationReq {
    private String name;

    @NotNull(message = "Presentation code cannot be null")
    private String code;

    private String description;

    private EPresentType type;

    private ECommonStatus status;
}
