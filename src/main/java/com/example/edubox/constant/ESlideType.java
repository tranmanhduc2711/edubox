package com.example.edubox.constant;

import com.example.edubox.converter.PersistentEnumConverter;
import com.example.edubox.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum ESlideType implements PersistentEnum<String> {

    QUESTION("QUESTION","QUESTION"),
    HEADING("HEADING","HEADING"),
    PARAGRAPH("PARAGRAPH","Paragraph");

    private static final Map<String, ESlideType> mapByValue;

    static {
        mapByValue = new HashMap<>();
        for (ESlideType e : values()) {
            mapByValue.put(e.getValue(), e);
        }
    }

    private ESlideType(String value, String displayName) {
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
    public static ESlideType of(String value) {
        return mapByValue.get(value);
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public static class Converter extends PersistentEnumConverter<ESlideType, String> {
        public Converter() {
            super(ESlideType.class);
        }
    }

    public static class RequestParamConverter implements org.springframework.core.convert.converter.Converter<String, ESlideType> {
        @Override
        public ESlideType convert(String source) {
            ESlideType taxType = of(source);
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
