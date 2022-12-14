package com.example.edubox.socket.message.send;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendResultPayload implements Serializable {
    private static long serialVersionUID = 3269223254253730148L;
    private String presentCode;
    private Integer A;
    private Integer B;
    private Integer C;
    private Integer D;
}
