package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.InvitationService;
import com.example.edubox.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/invitation")
@AllArgsConstructor
public class InvitationController extends BaseController {

    private final GroupService groupService;

    private final MailService mailService;

    private final InvitationService invitationService;


    @PostMapping("/sendMail")
    public ResponseEntity<?> inviteGroupMemberByMail(
            @RequestParam(value = "groupCode",required = true) String groupCode,
            @RequestParam(value = "email",required = true) String email
    ) {
        invitationService.
        return null;
    }
}
