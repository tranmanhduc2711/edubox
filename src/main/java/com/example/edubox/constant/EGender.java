package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum EGender implements PersistentEnum<String> {

    MALE("MALE","Male"),
    FEMALE("FEMALE","Female");

    private static final Map<String, EGender> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (EGender e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private EGender(String value, String displayName) {
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
    public static EGender of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<EGender, String> {
        public Converter() {
            super(EGender.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, EGender> {
        @Override
        public EGender convert(String source) {
            EGender taxType = of(source);
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
