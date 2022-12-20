package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum EventType implements PersistentEnum<String> {
    JOIN_GAME("JOIN_GAME", "JOIN_GAME"),
    START_GAME("START_GAME", "START_GAME"),
    SEND_ANSWER("SEND_ANSWER", "SEND_ANSWER"),
    RECEIVE_ANSWER("RECEIVE_ANSWER", "RECEIVE_ANSWER"),
    SEND_RESULT("SEND_RESULT", "SEND_RESULT"),
    RECEIVE_RESULT("RECEIVE_RESULT", "RECEIVE_RESULT"),
    NEXT_SLIDE("NEXT_SLIDE", "NEXT_SLIDE"),
    LEAVE_GAME("LEAVE_GAME", "LEAVE_GAME"),
    COUNTDOWN("COUNTDOWN", "COUNTDOWN"),
    END_GAME("END_GAME", "END_GAME");

    private static final Map<String, EventType> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (EventType e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private EventType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private final String value;

    private final String displayName;

    @Override
    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static EventType of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<EventType, String> {
        public Converter() {
            super(EventType.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, EventType> {
        @Override
        public EventType convert(String source) {
            EventType commonStatus = of(source);
            if (commonStatus == null) {
                throw new BusinessException(
                        ErrorCode.BAD_REQUEST,
                        String.format("Status is mismatch, status value: %s", source)
                );
            }

            return commonStatus;
        }
    }
}
