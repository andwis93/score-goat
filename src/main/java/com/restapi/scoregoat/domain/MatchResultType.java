package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MatchResultType {
    UNSET(0),
    HOME(1),
    AWAY(2),
    DRAW(3);
    private final int result;
}
