package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailTypes {
    RESET("reset"),
    VERIFY("verify");

    private final String type;
}
