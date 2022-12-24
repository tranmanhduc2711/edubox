package com.example.edubox.model.req;

import com.example.edubox.constant.ESlideType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
public class SlideReq{
    private ESlideType type;
    private String heading;
    private String paragraph;
    private Integer timer;
    private List<SlideChoiceReq> choices;
}