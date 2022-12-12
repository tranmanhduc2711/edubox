package com.example.edubox.service;

import com.example.edubox.constant.EPresentType;
import com.example.edubox.entity.Presentation;
import com.example.edubox.model.req.CreatePresentationReq;
import com.example.edubox.model.req.UpdatePresentationReq;
import com.example.edubox.model.res.PresentationRes;

import java.util.List;

public interface PresentationService {
    List<Presentation> getPresentations(
            EPresentType type,
            String code);

    PresentationRes create(CreatePresentationReq req);

    PresentationRes update(UpdatePresentationReq req);

    PresentationRes delete(String presentCode);

    Presentation findActive(String code);
}
