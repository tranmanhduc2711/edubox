package com.example.edubox.service;

import com.example.edubox.entity.User;

public interface MailService {
    void sendMailEventListener(User user);
    void sendMail(String groupCode,String email);
}
