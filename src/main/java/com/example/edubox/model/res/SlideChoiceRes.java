package com.example.edubox.model.res;

import com.example.edubox.entity.SlideChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SlideChoiceRes {
    private String icon;
    private Boolean isCorrect;
    private String answer;

    public static SlideChoiceRes valueOf(SlideChoice slideChoice){
        return SlideChoiceRes.builder()
                .icon(slideChoice.getIcon())
                .isCorrect(slideChoice.getIsCorrect())
                .answer(slideChoice.getAnswer())
                .build();
    }
}
