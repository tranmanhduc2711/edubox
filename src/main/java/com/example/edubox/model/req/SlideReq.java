package com.example.edubox.model.req;

import com.example.edubox.constant.ESlideType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class SlideReq{
    @NotNull(message = "type cannot be null")
    private ESlideType type;
    private String heading;
    private String paragraph;
    private Integer timer;
    private List<SlideChoiceReq> choices;
}