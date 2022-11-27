package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum ERoleType implements PersistentEnum<String> {

    MEMBER("MEMBER","Member"),
    OWNER("OWNER","Owner"),
    KICK_OUT("KICK_OUT","Kick out");

    private static final Map<String, ERoleType> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (ERoleType e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private ERoleType(String value, String displayName) {
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
    public static ERoleType of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<ERoleType, String> {
        public Converter() {
            super(ERoleType.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, ERoleType> {
        @Override
        public ERoleType convert(String source) {
            ERoleType taxType = of(source);
            if (taxType == null) {
                throw new BusinessException(
                        ErrorCode.BAD_REQUEST,
                        String.format("Tax type is mismatch, tax type value : %s", source)
                );
            }

            return taxType;
        }
    }
}
