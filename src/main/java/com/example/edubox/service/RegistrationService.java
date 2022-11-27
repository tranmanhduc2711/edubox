package com.example.edubox.service;

import com.example.edubox.entity.User;
import com.example.edubox.model.req.CreateUserReq;

public interface RegistrationService {
    public String confirmToken(String token);
    public User register(CreateUserReq createUserReq);
}
