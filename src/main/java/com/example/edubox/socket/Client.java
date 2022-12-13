package com.example.edubox.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

@Setter
@Getter
@AllArgsConstructor
public class Client implements Principal {
    private String name;

}
