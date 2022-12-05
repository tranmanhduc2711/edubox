package com.example.edubox.service.impl;

import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.User;
import com.example.edubox.entity.VerificationToken;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreateUserReq;
import com.example.edubox.model.res.UserRes;
import com.example.edubox.service.RegistrationService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public String confirmToken(String token) {
        VerificationToken verificationToken =
                userService.getVerificationToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        User user = verificationToken.getUser();
        if (user.getIsEnabled().equals(Boolean.TRUE)) {
            throw new BusinessException(ErrorCode.TOKEN_IS_ALREADY_CONFIRM, "Token is already confirmed");
        }
        user.setIsEnabled(true);
        userService.saveUser(user);
        return "Confirmed";
    }

    @Override
    @Transactional
    public UserRes register(CreateUserReq createUserReq) {
        User user = userService.createUser(createUserReq);
        eventPublisher.publishEvent(user);
        return UserRes.valueOf(user);
    }
}
