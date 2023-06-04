package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MatchStatusType {
    FINISHED("Match Finished"),
    NOT_STARTED("Not Started");

    private final String type;
}
