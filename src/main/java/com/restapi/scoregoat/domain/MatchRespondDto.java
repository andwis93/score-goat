package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchRespondDto {
    private int leagueId;
    private String respond;

    public MatchRespondDto(String respond) {
        this.respond = respond;
    }
}
