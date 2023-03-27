package com.restapi.scoregoat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MatchDto {
    private Long id;
    private int leagueId;
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
