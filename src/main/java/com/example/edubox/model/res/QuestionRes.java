package com.example.edubox.model.res;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRes {
    private String title;
    private PresentationRes presentationRes;
    private Boolean isReplied;
    private LocalDateTime postDate;
    private Integer vote;
    private ECommonStatus status;

    public static QuestionRes valueOf(Question question){
        return QuestionRes.builder()
                .title(question.getTitle())
                .isReplied(question.getIsAnswered())
                .postDate(question.getPostDate())
                .vote(question.getVote())
                .status(question.getStatus())
                .build();
    }
}
