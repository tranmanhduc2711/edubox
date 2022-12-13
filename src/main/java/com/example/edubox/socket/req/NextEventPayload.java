package com.example.edubox.socket.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NextEventPayload {
    private Integer itemNo;
    private String presentCode;
}
