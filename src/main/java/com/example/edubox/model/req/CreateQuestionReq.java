package com.example.edubox.model.req;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateQuestionReq {
    private String title;

    @NotNull(message = "Presentation code cannot null")
    private String presentCode;
}
