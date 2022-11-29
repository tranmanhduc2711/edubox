package com.example.edubox.support.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {
    public static final String MESSAGE_FORMAT = "message_%d";

    private final ResourceBundleMessageSource messageSource;


    @Autowired
    public Translator(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }

    public String toLocale(int errorCode) {
        String msgCode = String.format(MESSAGE_FORMAT, errorCode);
        return toLocale(msgCode);
    }
}
