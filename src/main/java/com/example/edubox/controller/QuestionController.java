package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.CreateQuestionReq;
import com.example.edubox.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/question-management")
@AllArgsConstructor
public class QuestionController extends BaseController {
    private final QuestionService questionService;

    @GetMapping("")
    ResponseEntity<?> getListQuestions(
            @RequestParam(value = "presentCode",required = false) String presentCode,
            @RequestParam(value = "isAnswered",required = false) Boolean isAnswered,
            @RequestParam(value = "total",required = false) Integer vote,
            @RequestParam(value = "from",required = false) LocalDateTime from,
            @RequestParam(value = "to",required = false) LocalDateTime to){
        return success(questionService.getListQuestion(presentCode,isAnswered,vote,from,to));
    }

    @PostMapping("")
    ResponseEntity<?> createQuestion(@RequestBody CreateQuestionReq createQuestionReq){
        return success(questionService.create(createQuestionReq));
    }

    @GetMapping("/upvote")
    ResponseEntity<?> upvoteQuestion(@RequestParam(value = "questionCode",required = true) String code){
        questionService.upvote(code);
        return success(null);
    }

    @GetMapping("/mark-answered")
    ResponseEntity<?> markAnswered(@RequestParam(value = "questionCode",required = true) String code){
        questionService.markAnswered(code);
        return success(null);
    }

}
