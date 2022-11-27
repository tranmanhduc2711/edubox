package com.example.edubox.service.impl;

import com.example.edubox.entity.Group;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Autowired
    private GroupService groupService;

    @Override
    public String getGroupInvitation(String groupCode) {
        return null;
    }
}
