package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.AddSlidesReq;
import com.example.edubox.model.req.UpdateSlideReq;
import com.example.edubox.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slides")
public class SlideController extends BaseController {
    @Autowired
    private SlideService slideService;

    @PostMapping("/add")
    ResponseEntity<?> createSlidesForPresentation(@RequestBody AddSlidesReq slidesReq) {
        return success(slideService.addSlidesForPresentation(slidesReq));
    }

    @GetMapping("")
    ResponseEntity<?> getSlides(
            @RequestParam(value = "presentCode", required = true) String code,
            @RequestParam(value = "itemNo", required = false) Integer itemNo) {
        return success(slideService.getSlidesByPresentation(code,itemNo));
    }

    @GetMapping("/delete")
    ResponseEntity<?> deleteSlide(
            @RequestParam(value = "presentCode", required = true) String presentCode,
            @RequestParam(value = "itemNo", required = true) Integer itemNo) {
        return success(slideService.deleteSlide(presentCode, itemNo));
    }

    @PostMapping("/update")
    ResponseEntity<?> update(@RequestBody UpdateSlideReq req) {
        return success(slideService.updateSlide(req));
    }
}
