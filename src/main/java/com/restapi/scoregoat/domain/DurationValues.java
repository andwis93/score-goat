package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DurationValues {
    ATTEMPT_BLOCKED(24),
    MAX_ATTEMPT(5),
    SESSION_LENGTH(10);

    private final int value;

}
