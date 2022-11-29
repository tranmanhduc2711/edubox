package com.example.edubox.service;

import com.example.edubox.entity.User;
import com.example.edubox.model.req.CreateUserReq;
import com.example.edubox.model.res.UserRes;

public interface RegistrationService {
    public String confirmToken(String token);
    public UserRes register(CreateUserReq createUserReq);
}
