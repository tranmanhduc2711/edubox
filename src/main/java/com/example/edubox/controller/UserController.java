package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.res.UserRes;
import com.example.edubox.service.GroupMemberService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-management")
@AllArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final GroupMemberService groupMemberService;

    @GetMapping
    ResponseEntity<?> getUser(@RequestParam(value = "code",required = true) String code) {
        UserRes user = UserRes.valueOf(userService.findByUsername(code));
        return success(user);
    }

    @GetMapping(value = "/groups")
    ResponseEntity<?> getUserGroup(@RequestParam(value = "code",required = true) String code) {
        return success(groupMemberService.getGroupsByUserCode(code));
    }

}
