package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.Slide;
import com.example.edubox.entity.SlideChoice;
import com.example.edubox.entity.User;
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
import com.example.edubox.service.SlideService;
import com.example.edubox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserService userService;

    @Override
    @Transactional
    public String addSlidesForPresentation(AddSlidesReq addSlidesReq) {
        Presentation presentation = presentationService.findActive(addSlidesReq.getPresentCode());
        createSlides(addSlidesReq.getSlides(), presentation);
        return presentation.getCode();
    }

    @Override
    public List<SlideRes> getSlidesByPresentation(String code, Integer itemNo) {
        Presentation presentation = presentationService.findActive(code);
        List<Slide> slides = slideRepository.findSlides(presentation, itemNo, ECommonStatus.ACTIVE);
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
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = presentationService.findActive(presentCode);
        if(!user.equals(presentation.getHost())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED,"Permission denied");
        }
        Slide slide = slideRepository.findSlideByPresentationAndAndItemNo(presentation, itemNo)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.SLIDE_NOT_FOUND, "Slide not found")
                );
        slide.setStatus(ECommonStatus.INACTIVE);
        slideRepository.save(slide);
        presentation.decr(1);
        presentationRepository.save(presentation);
        rebuildPresentationItemNo(presentation);
        return SlideRes.valueOf(slide, null);
    }

    @Override
    public Presentation updateSlide(UpdateSlideReq updateSlideReqs) {
        Presentation presentation = presentationService.findActive(updateSlideReqs.getPresentCode());

        List<Slide> slideRecords = slideRepository.findSlideByPresentationAndStatus(presentation, ECommonStatus.ACTIVE);
        for (Slide s : slideRecords) {
            s.setStatus(ECommonStatus.INACTIVE);
            slideRepository.save(s);
        }
        updateSlidesRecord(updateSlideReqs.getSlides(),presentation);

        return presentation;
}

    public void createSlides(List<SlideReq> slides, Presentation presentation) {
        for (SlideReq slideReq : slides) {
            validateSlideChoiceReq(slideReq);
            Slide slide = new Slide();
            slide.setPresentation(presentation);
            slide.setQuestion(slideReq.getQuestion());
            slide.setTimer(slideReq.getTimer());
            slide.setStatus(ECommonStatus.ACTIVE);

            slideRepository.save(slide);
            createSlideChoices(slideReq.getChoices(), slide);
            if (presentation.getTotalSlide() == null) {
                presentation.setTotalSlide(0);
            }
            presentation.incr(1);
            presentationRepository.save(presentation);
        }
        rebuildPresentationItemNo(presentation);
    }
    public void updateSlidesRecord(List<SlideReq> slides, Presentation presentation) {
        presentation.setTotalSlide(0);
        for (SlideReq slideReq : slides) {
            validateSlideChoiceReq(slideReq);
            Slide slide = new Slide();
            slide.setPresentation(presentation);
            slide.setQuestion(slideReq.getQuestion());
            slide.setTimer(slideReq.getTimer());
            slide.setStatus(ECommonStatus.ACTIVE);

            slideRepository.save(slide);
            createSlideChoices(slideReq.getChoices(), slide);
            if (presentation.getTotalSlide() == null) {
                presentation.setTotalSlide(0);
            }
            presentation.incr(1);
        }
        presentationRepository.save(presentation);
        rebuildPresentationItemNo(presentation);
    }

    public List<SlideChoice> createSlideChoices(List<SlideChoiceReq> choiceReqs, Slide slide) {
        if (choiceReqs.size() > 4) {
            throw new BusinessException(ErrorCode.SLIDE_CHOICES_OVER_QUANTITY, "Slide choices over");
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
        if (CollectionUtils.isEmpty(slideReq.getChoices())) {
            throw new BusinessException(ErrorCode.SLIDE_CHOICES_CANNOT_EMPTY, "Slide choices cannot empty");
        }
    }

    private void rebuildPresentationItemNo(Presentation presentation) {
        List<Slide> slides = slideRepository.findSlides(presentation, null, ECommonStatus.ACTIVE);
        int count = 1;
        for (Slide s : slides) {
            s.setItemNo(count++);
            slideRepository.save(s);
        }
    }
}
