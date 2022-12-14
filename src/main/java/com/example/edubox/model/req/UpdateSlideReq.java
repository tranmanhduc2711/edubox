package com.example.edubox.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
public class UpdateSlideReq {
    private String presentCode;

    private Integer itemNo;

    @NotEmpty(message = "Question cannot be empty")
    private String questions;

    private Integer timer;

    private List<SlideChoiceReq> slideChoices;
}
