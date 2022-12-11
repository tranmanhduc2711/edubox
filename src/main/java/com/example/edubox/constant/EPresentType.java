package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum EPresentType implements PersistentEnum<String> {
    PUBLIC("PUBLIC", "Public"),
    PRIVATE("PRIVATE", "Private");

    private static final Map<String, EPresentType> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (EPresentType e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private EPresentType(String value, String displayName) {
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
    public static EPresentType of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<EPresentType, String> {
        public Converter() {
            super(EPresentType.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, EPresentType> {
        @Override
        public EPresentType convert(String source) {
            EPresentType commonStatus = of(source);
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
