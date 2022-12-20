package com.example.edubox.socket.message.send;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SendAnswerPayload implements Serializable {
    private static long serialVersionUID = 1746181366800328473L;

    private String token;

    private String presentCode;

    private List<String> choices;

    private Boolean isCorrect;
}
