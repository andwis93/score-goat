package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WindowStatus {
    CLOSE(0), OPEN(1);
    private final int status;
}
