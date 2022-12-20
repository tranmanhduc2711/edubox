package com.example.edubox.service;

import com.example.edubox.entity.Presentation;
import com.example.edubox.model.req.AddSlidesReq;
import com.example.edubox.model.req.UpdateSlideReq;
import com.example.edubox.model.res.PresentationRes;
import com.example.edubox.model.res.SlideRes;

import java.util.List;

public interface SlideService {
    String addSlidesForPresentation(AddSlidesReq addSlidesReq);

    List<SlideRes> getSlidesByPresentation(String code,Integer itemNo);

    SlideRes deleteSlide(String presentCode, Integer itemNo);

    Presentation updateSlide(UpdateSlideReq updateSlideReqs);
}
