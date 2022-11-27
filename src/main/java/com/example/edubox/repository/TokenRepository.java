package com.example.edubox.repository;

import com.example.edubox.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken,Integer> {
    Optional<VerificationToken> findVerificationTokenByToken(String token);
}
