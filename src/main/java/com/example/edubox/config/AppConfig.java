package com.example.edubox.config;

import com.example.edubox.security.jwt.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Properties;

@Configuration
public class AppConfig {
    @Value("${app.api.ignoring.antMatchers:}")
    private String[] apiIgnoringEndpoints;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JWTAuthorizationFilter permissionFilter() {
        return new JWTAuthorizationFilter(List.of(apiIgnoringEndpoints));
    }
}
