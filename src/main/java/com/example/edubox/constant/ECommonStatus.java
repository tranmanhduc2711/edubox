package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum ECommonStatus implements PersistentEnum<String> {
    INACTIVE("I", "Inactive"),
    ACTIVE("A", "Active");

    private static final Map<String, ECommonStatus> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (ECommonStatus e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private ECommonStatus(String value, String displayName) {
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
    public static ECommonStatus of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<ECommonStatus, String> {
        public Converter() {
            super(ECommonStatus.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, ECommonStatus> {
        @Override
        public ECommonStatus convert(String source) {
            ECommonStatus commonStatus = of(source);
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
