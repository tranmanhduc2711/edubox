package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ERoleType;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.GroupMember;
import com.example.edubox.entity.User;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreateGroupReq;
import com.example.edubox.model.req.RoleAssignmentReq;
import com.example.edubox.model.res.GroupRes;
import com.example.edubox.model.res.UserRes;
import com.example.edubox.repository.GroupMemberRepository;
import com.example.edubox.repository.GroupRepository;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.SequenceService;
import com.example.edubox.service.UserService;
import com.example.edubox.util.Strings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final static String GROUP_CODE = "group-code";
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;
    private final SequenceService sequenceService;


    @Override
    public GroupRes createGroup(CreateGroupReq createGroupReq) {
        Group group = new Group();
        group.setGroupName(createGroupReq.getName());
        group.setGroupCode(buildGroupCode());
        group.setDescription(createGroupReq.getDescription());
        groupRepository.save(group);

        User user = userService.findByCode(createGroupReq.getOwnerCode());
        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setGroup(group);
        groupMember.setRoleType(ERoleType.OWNER);
        groupMember.setStatus(ECommonStatus.ACTIVE);
        return GroupRes.valueOf(group,UserRes.valueOf(user));
    }

    @Override
    public List<GroupRes> getGroups() {
        List<Group> groups =  groupRepository.findAll();
        return  groups
                .stream()
                .map(group -> {
                        User user = groupMemberRepository.getGroupOwner(group.getGroupCode());
                        return GroupRes.valueOf(group,UserRes.valueOf(user));
                }).collect(Collectors.toList());
    }

    @Override
    public GroupRes getGroupDetail(String groupCode) {
        Group group = groupRepository.findByGroupCode(groupCode).orElseThrow(
                () -> new BusinessException(ErrorCode.GROUP_CODE_NOT_FOUND,"Group not found")
        );
        User user = groupMemberRepository.getGroupOwner(groupCode);
        return GroupRes.valueOf(group,UserRes.valueOf(user));
    }

    @Override
    public List<UserRes> getGroupMembers(String code) {
        List<User> users = groupMemberRepository.getGroupMembersByCode(code);
        return users.stream().map(UserRes::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<GroupRes> getGroupsCreatedByUser(String userCode) {
        List<Group> groups = groupMemberRepository.getGroupsCreatedByUser(userCode);
        return groups.stream().map(item -> GroupRes.valueOf(item,null)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroupMember assignMemberRole(RoleAssignmentReq roleAssignmentReq) {
        Optional<Group> group = groupRepository.findByGroupCode(roleAssignmentReq.getGroupCode());
        if(group.isEmpty()) {
            throw new BusinessException(ErrorCode.GROUP_CODE_NOT_FOUND,"Group not found");
        } else if(ECommonStatus.INACTIVE.equals(group.get().getStatus())) {
            throw new BusinessException(ErrorCode.GROUP_IS_INACTIVE,"Group is inactive");
        }

        Optional<User> member = groupMemberRepository.findMember(roleAssignmentReq.getGroupCode(),roleAssignmentReq.getUserCode());
        if(member.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND,"Member not found");
        } else if(ECommonStatus.INACTIVE.equals(group.get().getStatus())) {
            throw new BusinessException(ErrorCode.USER_IS_INACTIVE,"Member is inactive");
        }
        List<GroupMember> groupMembers = groupMemberRepository.findAllByUserAndStatus(member.get(),ECommonStatus.ACTIVE);

        for(GroupMember gr : groupMembers) {
            gr.setStatus(ECommonStatus.INACTIVE);
            groupMemberRepository.save(gr);
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group.get());
        groupMember.setUser(member.get());
        groupMember.setRoleType(roleAssignmentReq.getRoleType());
        groupMember.setStatus(ECommonStatus.ACTIVE);
        return  groupMemberRepository.save(groupMember);
    }

    @Override
    public Optional<Group> findByCode(String code) {
        return groupRepository.findByGroupCode(code);
    }

    @Override
    public Group findActiveGroup(String code) {
        return this.findByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.GROUP_CODE_NOT_FOUND,"Group not found")
        );
    }

    @Override
    public GroupMember assignToGroup(String groupCode, String userCode) {
        Group group = this.findActiveGroup(groupCode);
        User user = userService.findByCode(userCode);
        Optional<User> member = groupMemberRepository.findMember(groupCode,userCode);
        if(member.isPresent()) {
            throw new BusinessException(ErrorCode.USER_IS_ALREADY_IN_GROUP,"User is already in group");
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setRoleType(ERoleType.MEMBER);
        groupMember.setStatus(ECommonStatus.ACTIVE);
        return groupMemberRepository.save(groupMember);
    }

    private String buildGroupCode() {
        int yy = LocalDate.now().getYear() % 100;
        int nextSeq = sequenceService.getNextSeq(GROUP_CODE, yy);
        String seqVal = Strings.formatWithZeroPrefix(nextSeq, 4);

        return String.format("%s%s%s","GR", yy, seqVal);
    }
}
