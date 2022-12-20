package com.example.edubox.service;

import com.example.edubox.entity.Group;
import com.example.edubox.entity.GroupMember;
import com.example.edubox.model.req.CreateGroupReq;
import com.example.edubox.model.req.JoinGroupReq;
import com.example.edubox.model.req.RoleAssignmentReq;
import com.example.edubox.model.res.AssignMemberRoleRes;
import com.example.edubox.model.res.GroupRes;
import com.example.edubox.model.res.MemberRes;
import com.example.edubox.model.res.UserRes;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    GroupRes createGroup(CreateGroupReq createGroupReq);

    List<GroupRes> getGroups();

    GroupRes getGroupDetail(String groupCode);

    GroupRes deleteGroup(String groupCode);

    List<MemberRes> getGroupMembers(String code);
    List<GroupRes> getGroupsCreatedByUser(String userCode);

    AssignMemberRoleRes assignMemberRole(RoleAssignmentReq roleAssignmentReq);

    Optional<Group> findByCode(String code);
    Group findActiveGroup(String code);

    boolean assignToGroup(JoinGroupReq joinGroupReq);
    String joinByLink(JoinGroupReq joinGroupReq);
}
