package com.example.edubox.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class SlideChoiceReq {
    private String answer;

    @NotEmpty(message = "Missing correct value")
    private Boolean isCorrect;

    private String icon; // A,B,C,D
}
