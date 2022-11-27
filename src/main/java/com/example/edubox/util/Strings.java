package com.example.edubox.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Strings {
    public static String formatWithZeroPrefix(Object val, int length) {
        String template = "%0" + length + "d";
        return String.format(template, val);
    }
}
