package com.restapi.scoregoat.domain.client.TimeFrame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeFrame {
    DAYS(10);

    private final int timeFrame;

}
