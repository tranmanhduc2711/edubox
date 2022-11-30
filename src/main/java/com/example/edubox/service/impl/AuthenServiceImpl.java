package com.example.edubox.service.impl;

import com.example.edubox.service.AuthenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenServiceImpl implements AuthenService {
    @Override
    public String getUsernameByToken(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null;
    }
}
