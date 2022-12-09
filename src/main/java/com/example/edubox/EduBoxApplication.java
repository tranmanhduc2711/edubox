package com.example.edubox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EduBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduBoxApplication.class, args);
    }

}
