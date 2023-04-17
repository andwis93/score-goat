package com.restapi.scoregoat.domain.client.TimeFrame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeFrame {
    DAYS_BEFORE(10),
    DAYS_AFTER(10);

    private final int timeFrame;
}
