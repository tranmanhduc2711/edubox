package com.example.edubox.socket;

import com.example.edubox.constant.EMessageEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class Message<T> implements Serializable {
    private static final long serialVersionUID = -8331372477990012094L;

    private EMessageEvent event;

    private String token;

    private T payload;

}
