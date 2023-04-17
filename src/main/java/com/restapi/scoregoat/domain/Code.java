package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Code {
    SEASON_CLEAN("E001"),
    SEASON_SET_DB("E002"),
    SESSION_REFRESH_ERROR("E003"),
    LOGIN_RESET_ATTEMPT("E004"),
    FIXTURE_GET("E005"),
    MATCH_UPLOAD("E006"),
    MATCH_UPLOAD_ALL("E007"),
    COULD_NOT_EXECUTE_PREDICTION("E008"),
    SEASON_SET("I001"),
    USER_CREATE("I002"),
    USER_LOGIN("I003"),
    LOGIN_LOCKED_DATES_UPDATE("I004"),
    SESSION_EXPIRED_REMOVE("I005"),
    USER_PASSWORD_CHANGED("I006"),
    UPLOAD_MATCHES_OK("I007"),
    UPLOAD_ALL_MATCHES_OK("I008");

    private final String code;
}
