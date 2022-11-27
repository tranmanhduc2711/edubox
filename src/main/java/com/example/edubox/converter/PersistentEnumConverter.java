package com.example.edubox.converter;

import com.example.edubox.constant.PersistentEnum;

import javax.persistence.AttributeConverter;

public abstract class PersistentEnumConverter<T extends Enum<T> & PersistentEnum<E>, E> implements AttributeConverter<T, E> {

    private final Class<T> clazz;

    public PersistentEnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        T[] enums = clazz.getEnumConstants();
        for (T e : enums) {
            if (e.getValue().equals(dbData)) {
                return e;
            }
        }
        return null;
    }
}