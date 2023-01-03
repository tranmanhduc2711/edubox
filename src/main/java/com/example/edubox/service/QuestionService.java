package com.example.edubox.service;

import com.example.edubox.entity.Question;
import com.example.edubox.model.req.CreateQuestionReq;
import com.example.edubox.model.res.QuestionRes;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionService {
    Question findActive(String code);

    List<QuestionRes> getListQuestion(
            String presentCode,
            Boolean isAnswered,
            Integer vote,
            LocalDateTime from,
            LocalDateTime to
    );

    QuestionRes create(CreateQuestionReq createQuestionReq);

    void upvote(String questionCode);
    void markAnswered(String questionCode);
}
