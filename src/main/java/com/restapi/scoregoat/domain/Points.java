package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Points {
    NEUTRAL(0),
    WIN(3),
    LOSS(-1);

    private final int points;
}
