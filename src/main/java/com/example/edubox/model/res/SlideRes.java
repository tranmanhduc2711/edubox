package com.example.edubox.model.res;

import com.example.edubox.constant.ESlideType;
import com.example.edubox.entity.Slide;
import com.example.edubox.entity.SlideChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SlideRes {
    private Integer itemNo;
    private ESlideType type;
    private String heading;
    private String paragraph;
    private Integer timer;
    private List<SlideChoiceRes> choices;

    public static SlideRes valueOf(Slide slide, List<SlideChoiceRes> choices){
        return SlideRes.builder()
                .itemNo(slide.getItemNo())
                .type(slide.getSlideType())
                .heading(slide.getHeading())
                .paragraph(slide.getParagraph())
                .timer(slide.getTimer())
                .choices(choices)
                .build();
    }
}
