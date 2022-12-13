package com.example.edubox.service;

import com.example.edubox.entity.Slide;
import com.example.edubox.model.req.AddSlidesReq;
import com.example.edubox.model.req.UpdateSlideReq;
import com.example.edubox.model.res.SlideRes;

import java.util.List;

public interface SlideService {
    String addSlidesForPresentation(AddSlidesReq addSlidesReq);

    List<SlideRes> getSlidesByPresentation(String code,Integer itemNo);

    SlideRes deleteSlide(String presentCode, Integer itemNo);

    SlideRes updateSlide(UpdateSlideReq updateSlideReq);
    SlideRes getSlide(String code,Integer itemNo);
}
