package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum EMessageEvent implements PersistentEnum<String> {

    START("START","Start"),
    ANSWER("ANSWER","Answer"),
    NEXT("NEXT","Next"),
    RESULT("RESULT","Result");

    private static final Map<String, EMessageEvent> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (EMessageEvent e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private EMessageEvent(String value, String displayName) {
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
    public static EMessageEvent of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<EMessageEvent, String> {
        public Converter() {
            super(EMessageEvent.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, EMessageEvent> {
        @Override
        public EMessageEvent convert(String source) {
            EMessageEvent taxType = of(source);
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
