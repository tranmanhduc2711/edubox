package com.example.edubox.service;

import com.example.edubox.entity.User;
import com.example.edubox.entity.VerificationToken;
import com.example.edubox.model.req.CreateUserReq;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User createUser(CreateUserReq createUserReq);
    User getUser(User user);
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByCode(String code);
    Optional<VerificationToken> getVerificationToken(String verificationToken);
    void createVerificationToken(User user, String token);
}
