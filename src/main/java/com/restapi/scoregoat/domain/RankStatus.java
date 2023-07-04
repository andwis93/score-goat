package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RankStatus {
    NEUTRAL(0),
    FIRST(1),
    ABOVE(2),
    MIDDLE(3),
    UNDER(4),
    LAST(5);
    private final int status;

}
