package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.Question;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreateQuestionReq;
import com.example.edubox.model.res.PresentationRes;
import com.example.edubox.model.res.QuestionRes;
import com.example.edubox.repository.PresentationRepository;
import com.example.edubox.repository.QuestionRepository;
import com.example.edubox.service.PresentationService;
import com.example.edubox.service.QuestionService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final PresentationService presentationService;

    private final PresentationRepository presentationRepository;

    @Override
    public Question findActive(String code) {
        Question question = questionRepository.findQuestionByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.QUESTION_CODE_NOT_FOUND,"Question code not found")
        );
        if(question.getStatus().equals(ECommonStatus.INACTIVE)) {
            throw new BusinessException(ErrorCode.QUESTION_INACTIVE,"Question inactive");
        }
        return question;
    }

    @Override
    public List<QuestionRes> getListQuestion(String presentCode,
                                             Boolean isAnswered,
                                             Integer vote,
                                             LocalDateTime from,
                                             LocalDateTime to) {
        Optional<Presentation> presentationRecord = Optional.empty();
        if(StringUtils.isNotEmpty(presentCode)){
             presentationRecord = presentationRepository.findByCode(presentCode);
        }

        Presentation presentation = null;
        if(presentationRecord.isPresent()){
            presentation = presentationRecord.get();
        }
        List<Question> questions = questionRepository.getQuestions(presentation,isAnswered,vote,from,to, ECommonStatus.ACTIVE);
        List<QuestionRes> res = new ArrayList<>();
        for(Question q : questions) {
            QuestionRes  questionRes = QuestionRes.valueOf(q);
            questionRes.setPresentationRes(PresentationRes.valueOf(q.getPresentation()));
            res.add(questionRes);
        }
        return res;
    }

    @Override
    public QuestionRes create(CreateQuestionReq createQuestionReq) {
        Presentation presentation = presentationService.findActive(createQuestionReq.getPresentCode());

        Question question = new Question();
        question.setTitle(createQuestionReq.getTitle());
        question.setPresentation(presentation);
        question.setIsAnswered(Boolean.FALSE);
        question.setVote(0);
        question.setStatus(ECommonStatus.ACTIVE);
        question.setPostDate(LocalDateTime.now());
        questionRepository.save(question);
        return QuestionRes.valueOf(question);
    }

    @Override
    public void upvote(String questionCode) {
        Question question = findActive(questionCode);

        question.incr(1);
        questionRepository.save(question);
    }

    @Override
    public void markAnswered(String questionCode) {
        Question question = findActive(questionCode);

        question.setIsAnswered(Boolean.TRUE);
        questionRepository.save(question);
    }
}
