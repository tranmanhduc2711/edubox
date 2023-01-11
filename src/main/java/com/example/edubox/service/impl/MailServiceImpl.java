package com.example.edubox.service.impl;

import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.User;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.MailService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private String clientBasePath;
    private static final String serverBasePath = "https://edubox.azurewebsites.net";
    private static final String clientResetEndpoint = "/reset-password";

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
        String confirmationUrl = "http://localhost:3000/active/:" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("\r\n"  + confirmationUrl);
        mailSender.send(email);
    }

    @Override
    public void sendMail(String groupCode, String emailAddress) {
        Group group = groupService.findActiveGroup(groupCode);

        String recipientAddress = emailAddress;
        String subject = "Edubox Invitation";
        String invitationLink = "/edu/invitation/";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("\r\n" + String.format("Invitation to join Group %s by link:", group.getGroupName()));
        email.setText("\r\n" + invitationLink);
        mailSender.send(email);
    }

    @Override
    public void sendMailResetPassword(String username) {
        User user = userService.findByEmail(username).orElseThrow(
                () -> new BusinessException(ErrorCode.USERNAME_NOT_FOUND,
                        String.format("Username %s not found", username)
                ));
        String emailAddress = username;
        String subject = "Edubox Reset Password";
        String resetClientPage = clientBasePath + clientResetEndpoint;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailAddress);
        email.setSubject(subject);
        email.setText("\r\n" + String.format("Hello %s,please click below link to reset password!", username));
        email.setText("\r\n" + resetClientPage);
        mailSender.send(email);
    }
}
