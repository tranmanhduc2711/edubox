package com.example.edubox.model.res;

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
    private String question;
    private List<SlideChoiceRes> choices;

    public static SlideRes valueOf(Slide slide, List<SlideChoiceRes> choices){
        return SlideRes.builder()
                .itemNo(slide.getItemNo())
                .question(slide.getQuestion())
                .choices(choices)
                .build();
    }
}
