package com.example.edubox.socket.message.receive;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReceiveAnswerPayload implements Serializable {
    private static long serialVersionUID = -514494562813387968L;

    private String presentCode;

    private String username;

    private List<String> choices;

    private Boolean isCorrect;
}
