package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MatchDto {
    private Long id;
    private int leagueId;
    private Long fixtureId;
    private String date;
    private String status;
    private String elapsed;
    private String homeTeam;
    private String homeLogo;
    private boolean homeWinner;
    private String awayTeam;
    private String awayLogo;
    private boolean awayWinner;
    private int homeGoals;
    private int awayGoals;
}
