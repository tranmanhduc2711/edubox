package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ERoleType;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.GroupMember;
import com.example.edubox.entity.User;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreateGroupReq;
import com.example.edubox.model.req.JoinGroupReq;
import com.example.edubox.model.req.RoleAssignmentReq;
import com.example.edubox.model.res.GroupRes;
import com.example.edubox.model.res.MemberRes;
import com.example.edubox.model.res.UserRes;
import com.example.edubox.repository.GroupMemberRepository;
import com.example.edubox.repository.GroupRepository;
import com.example.edubox.repository.UserRepository;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.SequenceService;
import com.example.edubox.service.UserService;
import com.example.edubox.util.Strings;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final static String GROUP_CODE = "group-code";
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;

    private final UserRepository userRepository;
    private final SequenceService sequenceService;


    @Override
    public GroupRes createGroup(CreateGroupReq createGroupReq) {
        Group group = new Group();
        group.setGroupName(createGroupReq.getName());
        UUID uuid = UUID.randomUUID();
        group.setGroupCode(uuid.toString());
        group.setDescription(createGroupReq.getDescription());
        group.setStatus(ECommonStatus.ACTIVE);
        group.setCapacity(createGroupReq.getCapacity());
        groupRepository.save(group);

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);
        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setGroup(group);
        groupMember.setRoleType(ERoleType.OWNER);
        groupMember.setStatus(ECommonStatus.ACTIVE);
        groupMemberRepository.save(groupMember);
        return GroupRes.valueOf(group, UserRes.valueOf(user));
    }

    @Override
    public List<GroupRes> getGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups
                .stream()
                .map(group -> {
                    User user = groupMemberRepository.getGroupOwner(group.getGroupCode());
                    return GroupRes.valueOf(group, UserRes.valueOf(user));
                }).collect(Collectors.toList());
    }

    @Override
    public GroupRes getGroupDetail(String groupCode) {
        Group group = groupRepository.findByGroupCode(groupCode).orElseThrow(
                () -> new BusinessException(ErrorCode.GROUP_CODE_NOT_FOUND, "Group not found")
        );
        User user = groupMemberRepository.getGroupOwner(groupCode);
        return GroupRes.valueOf(group, UserRes.valueOf(user));
    }

    @Override
    public List<MemberRes> getGroupMembers(String code) {
        List<GroupMember> users = groupMemberRepository.getGroupMembersByCode(code);
        return users.stream().map(item -> MemberRes.valueOf(item.getUser(), item.getRoleType())).collect(Collectors.toList());
    }

    @Override
    public List<GroupRes> getGroupsCreatedByUser(String userCode) {
        List<Group> groups = groupMemberRepository.getGroupsCreatedByUser(userCode);
        return groups.stream().map(item -> GroupRes.valueOf(item, null)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean assignMemberRole(RoleAssignmentReq roleAssignmentReq) {

        Optional<Group> group = groupRepository.findByGroupCode(roleAssignmentReq.getGroupCode());
        if (group.isEmpty()) {
            return false;
        }

        Optional<User> user = userRepository.findByCode(roleAssignmentReq.getUserCode());
        if (user.isEmpty()) {
            return false;
        }
        List<GroupMember> groupMembers = groupMemberRepository.findAllByUserAndStatusAndGroup(user.get(), ECommonStatus.ACTIVE, group.get());

        for (GroupMember gr : groupMembers) {
            gr.setStatus(ECommonStatus.INACTIVE);
            groupMemberRepository.save(gr);
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group.get());
        groupMember.setUser(user.get());
        groupMember.setRoleType(roleAssignmentReq.getRoleType());
        groupMember.setStatus(ECommonStatus.ACTIVE);
        groupMemberRepository.save(groupMember);
        return true;
    }

    @Override
    public Optional<Group> findByCode(String code) {
        return groupRepository.findByGroupCode(code);
    }

    @Override
    public Group findActiveGroup(String code) {
        return this.findByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.GROUP_CODE_NOT_FOUND, "Group not found")
        );
    }

    @Override
    public boolean assignToGroup(JoinGroupReq joinGroupReq) {
        Group group = this.findActiveGroup(joinGroupReq.getGroupCode());
        User user = userService.findByUsername(joinGroupReq.getEmail());
        Optional<User> member = groupMemberRepository.findMember(joinGroupReq.getGroupCode(), user.getCode());
        if (member.isPresent()) {
            //throw new BusinessException(ErrorCode.USER_IS_ALREADY_IN_GROUP,"User is already in group");
            return false;
        }
        group.incr(1);
        groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setRoleType(joinGroupReq.getRoleType());
        groupMember.setStatus(ECommonStatus.ACTIVE);
        groupMemberRepository.save(groupMember);
        return true;
    }

    @Override
    public String joinByLink(JoinGroupReq joinGroupReq) {
        Group group = this.findActiveGroup(joinGroupReq.getGroupCode());
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);
        Optional<User> member = groupMemberRepository.findMember(joinGroupReq.getGroupCode(), user.getCode());
        if (member.isPresent()) {
            //throw new BusinessException(ErrorCode.USER_IS_ALREADY_IN_GROUP,"User is already in group");
            return null;
        }
        group.incr(1);
        groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setRoleType(ERoleType.MEMBER);
        groupMember.setStatus(ECommonStatus.ACTIVE);
        groupMemberRepository.save(groupMember);
        return group.getGroupCode();
    }

    private String buildGroupCode() {
        int yy = LocalDate.now().getYear() % 100;
        int nextSeq = sequenceService.getNextSeq(GROUP_CODE, yy);
        String seqVal = Strings.formatWithZeroPrefix(nextSeq, 4);

        return String.format("%s%s%s", "GR", yy, seqVal);
    }
}
