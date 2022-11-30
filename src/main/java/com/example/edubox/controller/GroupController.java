package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.CreateGroupReq;
import com.example.edubox.model.req.JoinGroupReq;
import com.example.edubox.model.req.RoleAssignmentReq;
import com.example.edubox.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/group-management")
@AllArgsConstructor
public class GroupController extends BaseController {
    private final GroupService groupService;

    @PostMapping()
    public ResponseEntity<?> createGroup(@RequestBody @Valid CreateGroupReq createGroupReq) {
        return success(groupService.createGroup(createGroupReq));
    }
    @GetMapping("")
    public ResponseEntity<?> getGroups() {
        return success(groupService.getGroups());
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getGroupDetail(@RequestParam(value = "groupCode",required = true) String groupCode) {
        return success(groupService.getGroupDetail(groupCode));
    }

    @GetMapping("/member")
    public ResponseEntity<?> getGroupMembers(@RequestParam(value = "groupCode", required = true) String code) {
        return success(groupService.getGroupMembers(code));
    }

    @GetMapping("/created-by")
    public ResponseEntity<?> getGroupsCreatedBy(@RequestParam(value = "userCode", required = true) String code) {
        return success(groupService.getGroupsCreatedByUser(code));
    }

    @PostMapping("/assign")
    public Boolean assignRole(@RequestBody RoleAssignmentReq roleAssignmentReq) {
        return groupService.assignMemberRole(roleAssignmentReq);
    }

    @PostMapping("/add")
    public Boolean addMemberToGroup(@RequestBody JoinGroupReq joinGroupReq) {
        groupService.assignToGroup(joinGroupReq);
        return true;
    }

    @PostMapping("/join")
    public String joinByLink(@RequestBody JoinGroupReq joinGroupReq) {
        return groupService.joinByLink(joinGroupReq);
    }

}
