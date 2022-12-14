package com.example.edubox.socket.message;

import com.example.edubox.constant.EMessageEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage<T> {
    private EMessageEvent event;
    private T payload;
}
