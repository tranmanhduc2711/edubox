package com.example.edubox.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
public class SlideReq{
    @NotEmpty(message = "Question cannot be empty")
    private String questions;

    private Integer timer;

    private List<SlideChoiceReq> slideChoices;
}