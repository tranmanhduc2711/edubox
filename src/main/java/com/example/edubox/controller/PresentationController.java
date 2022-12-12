package com.example.edubox.controller;


import com.example.edubox.constant.EPresentType;
import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.CreatePresentationReq;
import com.example.edubox.model.req.UpdatePresentationReq;
import com.example.edubox.service.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/presentations")
public class PresentationController extends BaseController {
    @Autowired
    private PresentationService presentationService;

    @GetMapping("")
    ResponseEntity<?> getPresentations(@RequestParam(value = "type", required = false) EPresentType type,
                                       @RequestParam(value = "code", required = false) String code) {
        return success(presentationService.getPresentations(type, code));
    }

    @PostMapping("")
    ResponseEntity<?> create(@RequestBody CreatePresentationReq req) {
        return success(presentationService.create(req));
    }

    @DeleteMapping("")
    ResponseEntity<?> delete(@RequestParam(value = "code", required = true) String code) {
        return success(presentationService.delete(code));
    }

    @PutMapping("")
    ResponseEntity<?> update(@RequestBody @Valid UpdatePresentationReq req) {
        return success(presentationService.update(req));
    }

    @GetMapping("/check-host")
    ResponseEntity<?> checkIsPresentationHost(@RequestParam(value = "presentCode", required = true) String code) {
        return success(presentationService.checkIsHost(code));
    }
}
