package com.example.edubox.service.impl;

import com.example.edubox.entity.Group;
import com.example.edubox.entity.GroupMember;
import com.example.edubox.entity.User;
import com.example.edubox.service.GroupMemberService;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.InvitationService;
import com.example.edubox.service.MailService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final GroupService groupService;

    private final UserService userService;

    private final GroupMemberService groupMemberService;

    private final MailService mailService;

    @Override
    public void sendInvitationMail(String groupCode, String email) {
        Group group = groupService.findActiveGroup(groupCode);
        mailService.sendMail(groupCode,email);
    }
}
