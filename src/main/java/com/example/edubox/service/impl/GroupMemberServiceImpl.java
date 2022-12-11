package com.example.edubox.service.impl;

import com.example.edubox.entity.Group;
import com.example.edubox.entity.User;
import com.example.edubox.model.res.GroupRes;
import com.example.edubox.model.res.UserRes;
import com.example.edubox.repository.GroupMemberRepository;
import com.example.edubox.service.GroupMemberService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final UserService userService;

    @Override
    public List<GroupRes> getGroupsByUserCode(String code) {
        User user = userService.findActiveUser(code);
        List<Group> groups = groupMemberRepository.getGroupsByUserCode(code);
        return groups
                .stream()
                .map(item -> {
                    User owner = groupMemberRepository.getGroupOwner(item.getGroupCode());
                    return GroupRes.valueOf(item, UserRes.valueOf(owner));
                })
                .collect(Collectors.toList());
    }
}
