package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserPredictionDto {
    private String homeLogo;
    private String homeTeam;
    private int homeGoal;
    private String date;
    private String time;
    private int awayGoal;
    private String awayTeam;
    private String awayLogo;
    private String prediction;
    private int points;
    private String result;
}
