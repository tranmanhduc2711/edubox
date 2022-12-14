package com.example.edubox.socket.message.send;

import lombok.Data;

import java.io.Serializable;

@Data
public class PresentCodePayload implements Serializable {
    private static long serialVersionUID = 2593886067334870201L;
    private String presentCode;
}
