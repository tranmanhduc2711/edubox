package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.Slide;
import com.example.edubox.entity.SlideChoice;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.AddSlidesReq;
import com.example.edubox.model.req.SlideChoiceReq;
import com.example.edubox.model.req.SlideReq;
import com.example.edubox.model.req.UpdateSlideReq;
import com.example.edubox.model.res.SlideChoiceRes;
import com.example.edubox.model.res.SlideRes;
import com.example.edubox.repository.PresentationRepository;
import com.example.edubox.repository.SlideChoiceRepository;
import com.example.edubox.repository.SlideRepository;
import com.example.edubox.service.PresentationService;
import com.example.edubox.service.SequenceService;
import com.example.edubox.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlideServiceImpl implements SlideService {
    private static String SLIDE_CODE = "slide-code";
    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private PresentationRepository presentationRepository;

    @Autowired
    private SlideChoiceRepository slideChoiceRepository;
    @Autowired
    private PresentationService presentationService;
    @Autowired
    private SequenceService sequenceService;

    @Override
    @Transactional
    public String addSlidesForPresentation(AddSlidesReq addSlidesReq) {
        Presentation presentation = presentationService.findActive(addSlidesReq.getPresentCode());
        createSlides(addSlidesReq.getSlides(), presentation);
        return presentation.getCode();
    }

    @Override
    public List<SlideRes> getSlidesByPresentation(String code,Integer itemNo) {
        Presentation presentation = presentationService.findActive(code);
        List<Slide> slides = slideRepository.findSlides(presentation,itemNo, ECommonStatus.ACTIVE);
        List<SlideRes> slidesRes = new ArrayList<>();
        for (Slide slide : slides) {
            List<SlideChoice> slideChoices =
                    slideChoiceRepository.findSlideChoices(slide, ECommonStatus.ACTIVE);
            slidesRes.add(SlideRes.valueOf(slide,
                    slideChoices.stream().map(SlideChoiceRes::valueOf).collect(Collectors.toList())));
        }
        return slidesRes;
    }

    @Override
    public SlideRes deleteSlide(String presentCode, Integer itemNo) {
        Presentation presentation = presentationService.findActive(presentCode);
        Slide slide = slideRepository.findSlideByPresentationAndAndItemNo(presentation, itemNo)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.SLIDE_NOT_FOUND, "Slide not found")
                );
        slide.setStatus(ECommonStatus.INACTIVE);
        slideRepository.save(slide);
        rebuildPresentationItemNo(presentation);
        return SlideRes.valueOf(slide, null);
    }

    @Override
    public SlideRes updateSlide(UpdateSlideReq updateSlideReq) {
        Presentation presentation = presentationService.findActive(updateSlideReq.getPresentCode());
        Slide slide = slideRepository.findSlideByPresentationAndAndItemNo(presentation, updateSlideReq.getItemNo())
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.SLIDE_NOT_FOUND, "Slide not found")
                );
        List<SlideChoice> choices = slideChoiceRepository.findSlideChoices(slide, ECommonStatus.ACTIVE);
        // update slide
        slide.setQuestion(updateSlideReq.getQuestions());
        for (SlideChoice slideChoice : choices) {
            slideChoice.setStatus(ECommonStatus.INACTIVE);
            slideChoiceRepository.save(slideChoice);
        }
        slideRepository.save(slide);
        List<SlideChoice> newSlideChoices =
                createSlideChoices(updateSlideReq.getSlideChoices(), slide);
        return SlideRes.valueOf(slide, newSlideChoices.stream().map(SlideChoiceRes::valueOf).collect(Collectors.toList()));
    }

    public void createSlides(List<SlideReq> slides, Presentation presentation) {
        int count = 0;
        for (SlideReq slideReq : slides) {
            validateSlideChoiceReq(slideReq);
            Slide slide = new Slide();
            slide.setItemNo(count++);
            slide.setPresentation(presentation);
            slide.setQuestion(slideReq.getQuestions());
            slide.setStatus(ECommonStatus.ACTIVE);

            slideRepository.save(slide);
            createSlideChoices(slideReq.getSlideChoices(), slide);
            presentationRepository.save(presentation);
        }
    }

    public List<SlideChoice> createSlideChoices(List<SlideChoiceReq> choiceReqs, Slide slide) {
        if(choiceReqs.size() > 4){
            throw new BusinessException(ErrorCode.SLIDE_CHOICES_OVER_QUANTITY,"Slide choices over");
        }
        List<SlideChoice> res = new ArrayList<>();
        for (SlideChoiceReq choiceReq : choiceReqs) {
            SlideChoice choice = new SlideChoice();
            choice.setSlide(slide);
            choice.setIcon(choiceReq.getIcon());
            choice.setIsCorrect(choiceReq.getIsCorrect());
            choice.setAnswer(choiceReq.getAnswer());
            choice.setStatus(ECommonStatus.ACTIVE);

            res.add(choice);
            slideChoiceRepository.save(choice);
        }
        return res;
    }

    private void validateSlideChoiceReq(SlideReq slideReq) {
        if (CollectionUtils.isEmpty(slideReq.getSlideChoices())) {
            throw new BusinessException(ErrorCode.SLIDE_CHOICES_CANNOT_EMPTY, "Slide choices cannot empty");
        }
    }

    private void rebuildPresentationItemNo(Presentation presentation) {
        List<Slide> slides = slideRepository.findSlides(presentation,null,ECommonStatus.ACTIVE);
        int count = 0;
        for (Slide s : slides){
            s.setItemNo(count++);
            slideRepository.save(s);
        }
    }
}