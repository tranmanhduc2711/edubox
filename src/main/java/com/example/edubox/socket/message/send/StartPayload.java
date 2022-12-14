package com.example.edubox.socket.message.send;

import lombok.Data;

import java.io.Serializable;

@Data
public class StartPayload implements Serializable {
    private static long serialVersionUID = 5599697112117022310L;
    private String presentCode;
}
