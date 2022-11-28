package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.User;
import com.example.edubox.entity.VerificationToken;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreateUserReq;
import com.example.edubox.repository.TokenRepository;
import com.example.edubox.repository.UserRepository;
import com.example.edubox.service.SequenceService;
import com.example.edubox.service.UserService;
import com.example.edubox.util.Strings;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final String USER_CODE = "user-code";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final SequenceService sequenceService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createUser(CreateUserReq createUserReq) {
        User user = new User();
        user.setUsername(createUserReq.getUsername());
        user.setPassword(passwordEncoder.encode(createUserReq.getPassword()));
        user.setFullName(createUserReq.getFullName());
        user.setCode(buildUserCode());
        user.setEmail(createUserReq.getEmail());
        user.setGender(createUserReq.getGender());
        user.setAge(createUserReq.getAge());
        user.setIsEnabled(Boolean.FALSE);
        user.setStatus(ECommonStatus.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public User getUser(User user) {
        return userRepository.findByUsername(user.getUsername());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByCode(String code) {
        return userRepository.findByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND,String.format("User code not found: %s",code))
        );
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String verificationToken) {
        return tokenRepository.findVerificationTokenByToken(verificationToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    private String buildUserCode() {
        int yy = LocalDate.now().getYear() % 100;
        int nextSeq = sequenceService.getNextSeq(USER_CODE, yy);
        String seqVal = Strings.formatWithZeroPrefix(nextSeq, 4);

        return String.format("%s%s%s","US", yy, seqVal);
    }
}
