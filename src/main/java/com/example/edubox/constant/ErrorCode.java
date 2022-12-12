package com.example.edubox.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200),
    NOT_FOUND(404),
    BAD_REQUEST(400),
    SERVER_ERROR(500),
    UNAUTHORIZED(401),
    ACCESS_DENIED(403),

    /*
    *     Business error code 400XXX
    * */

    // User
    USER_NOT_FOUND(400100),
    TOKEN_NOT_FOUND(400101),
    TOKEN_IS_ALREADY_CONFIRM(400102),
    USER_IS_INACTIVE(400103),
    EMAIL_IS_USED(400104),

    // group
    GROUP_CODE_NOT_FOUND(400201),
    GROUP_IS_INACTIVE(400202),

    // group member
    GROUP_MEMBER_IS_OUT(400301),
    USER_IS_ALREADY_IN_GROUP(400302),
    PRESENTATION_CODE_NOT_FOUND(400401),
    PRESENTATION_CODE_INACTIVE(400402),
    SLIDE_CHOICES_CANNOT_EMPTY(400501),
    SLIDE_CHOICES_INVALID(400502),
    SLIDE_NOT_FOUND(400503),
    SLIDE_CHOICES_OVER_QUANTITY(400504);
    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return this.name();
    }
}
