package com.example.edubox.service;

import com.example.edubox.entity.User;
import com.example.edubox.entity.VerificationToken;
import com.example.edubox.model.req.CreateUserReq;
import com.example.edubox.model.req.UpdatePasswordReq;
import com.example.edubox.model.req.UpdateUserReq;
import com.example.edubox.model.res.UserRes;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User createUser(CreateUserReq createUserReq);
    UserRes updateUser(UpdateUserReq updateUserReq);
    void updatePassword(UpdatePasswordReq updatePasswordReq);
    User getUser(User user);
    User getAccountProfile();
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findActiveUser(String code);
    Optional<VerificationToken> getVerificationToken(String verificationToken);
    void createVerificationToken(User user, String token);
}
