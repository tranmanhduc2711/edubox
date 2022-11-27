package com.example.edubox.service.impl;

import com.example.edubox.entity.Group;
import com.example.edubox.entity.User;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.MailService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    private final UserService userService;
    private final GroupService groupService;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    @Async("backgroundProcessThreadPool")
    @TransactionalEventListener
    public void sendMailEventListener(User user) {
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "/registration/confirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("\r\n" + "http://localhost:8080/edu" + confirmationUrl);
        mailSender.send(email);
    }

    @Override
    public void sendMail(String groupCode, String emailAddress) {
        Group group = groupService.findActiveGroup(groupCode);

        String recipientAddress = emailAddress;
        String subject = "Edubox Invitation";
        String invitationLink = "/edu//invitation/";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("\r\n"  + String.format("Invitation to join Group %s by link:",group.getGroupName()));
        email.setText("\r\n" + invitationLink);
    }
}
