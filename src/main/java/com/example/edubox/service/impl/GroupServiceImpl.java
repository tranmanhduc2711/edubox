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
import com.example.edubox.model.res.AssignMemberRoleRes;
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
        group.setGroupCode(buildGroupCode());
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
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);
        List<Group> groups = groupMemberRepository.getGroupsByUserCode(user.getCode());
        return groups
                .stream()
                .map(group -> {
                    User userTmp = groupMemberRepository.getGroupOwner(group.getGroupCode());
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
    public GroupRes deleteGroup(String groupCode) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);
        Optional<User> owner = groupMemberRepository.findGroupOwner(groupCode);
        if(!user.getUsername().equals(owner.get().getUsername())){
            throw new BusinessException(ErrorCode.ACCESS_DENIED,"Member do not have permission");
        }
        Group group = findActiveGroup(groupCode);
        group.setStatus(ECommonStatus.INACTIVE);
        groupRepository.save(group);
        return GroupRes.valueOf(group,null);
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
    public AssignMemberRoleRes assignMemberRole(RoleAssignmentReq roleAssignmentReq) {
        validateRoleAssignment(roleAssignmentReq);
        Group group = findActiveGroup(roleAssignmentReq.getGroupCode());

        User user = userService.findActiveUser(roleAssignmentReq.getUserCode());
        List<GroupMember> groupMembers = groupMemberRepository.findAllByUserAndStatusAndGroup(user, ECommonStatus.ACTIVE, group);

        for (GroupMember gr : groupMembers) {
            gr.setStatus(ECommonStatus.INACTIVE);
            groupMemberRepository.save(gr);
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setRoleType(roleAssignmentReq.getRoleType());
        groupMember.setStatus(ECommonStatus.ACTIVE);
        groupMemberRepository.save(groupMember);

        AssignMemberRoleRes res = new AssignMemberRoleRes();
        res.setMemberCode(user.getCode());
        res.setFullName(user.getFullName());
        res.setUsername(user.getUsername());
        res.setRole(roleAssignmentReq.getRoleType());
        return res;
    }

    private void validateRoleAssignment(RoleAssignmentReq roleAssignmentReq) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User assignBy = userService.findByUsername(principal);

        ERoleType roleType = groupMemberRepository.getUserRoleType(roleAssignmentReq.getGroupCode(),assignBy);
        if(roleAssignmentReq.getRoleType().equals(ERoleType.CO_OWNER)) {
            if(!roleType.equals(ERoleType.OWNER)) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED,"Permission denied");
            }
        } else if(roleAssignmentReq.getRoleType().equals(ERoleType.KICK_OUT)) {
            if(roleType.equals(ERoleType.MEMBER)) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED,"Permission denied");
            }
        }
    }

    @Override
    public Optional<Group> findByCode(String code) {
        return groupRepository.findByGroupCode(code);
    }

    @Override
    public Group findActiveGroup(String code) {
        Group group = findByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.GROUP_CODE_NOT_FOUND, "Group not found")
        );
        if (ECommonStatus.INACTIVE.equals(group.getStatus())) {
            throw new BusinessException(ErrorCode.GROUP_IS_INACTIVE, "Group code is inactive");
        }
        return group;
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
