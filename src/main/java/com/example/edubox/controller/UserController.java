package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.UpdatePasswordReq;
import com.example.edubox.model.req.UpdateUserReq;
import com.example.edubox.model.res.UserRes;
import com.example.edubox.service.GroupMemberService;
import com.example.edubox.service.MailService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-management")
@AllArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final GroupMemberService groupMemberService;

    private final MailService mailService;

    @GetMapping
    ResponseEntity<?> getUser(@RequestParam(value = "userCode",required = true) String code) {
        UserRes user = UserRes.valueOf(userService.findActiveUser(code));
        return success(user);
    }

    @GetMapping("/getme")
    ResponseEntity<?> getAccountProfile() {
        return  success(UserRes.valueOf(userService.getAccountProfile()));
    }
    @PostMapping("/update")
    ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserReq updateUserReq) {
        return success(userService.updateUser(updateUserReq));
    }

    @GetMapping(value = "/groups")
    ResponseEntity<?> getUserGroup(@RequestParam(value = "userCode",required = true) String code) {
        return success(groupMemberService.getGroupsByUserCode(code));
    }

    @GetMapping(value = "/reset-password")
    ResponseEntity<?> sendMailResetPassword(@RequestParam(value = "username",required = true) String username) {
        mailService.sendMailResetPassword(username);
        return success(null);
    }

    @PostMapping("/update-password")
    ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordReq updatePasswordReq) {
        userService.updatePassword(updatePasswordReq);
        return success(null);
    }
}
