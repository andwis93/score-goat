package com.restapi.scoregoat.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SeasonConfig {
    DEFAULT_LEAGUE(39);

    private final int id;

}
