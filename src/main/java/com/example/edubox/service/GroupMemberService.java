package com.example.edubox.service;

import com.example.edubox.model.res.GroupRes;

import java.util.List;

public interface GroupMemberService {
    List<GroupRes> getGroupsByUserCode(String code);
}
