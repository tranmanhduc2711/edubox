package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.CreateUserReq;
import com.example.edubox.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping(value = "/registration")
public class RegistrationController extends BaseController {
    private final RegistrationService registrationService;

    @PostMapping(value = "")
    ResponseEntity<?> register(
            @RequestBody @Valid CreateUserReq createUserReq) {
        return success(registrationService.register(createUserReq));
    }

    @GetMapping(value = "/confirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
