package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MATCHES")
public class Match {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "LEAGUE_ID")
    private int leagueId;
    @Column(name = "FIXTURE_ID")
    private Long fixtureId;
    @Column(name = "DATE")
    private OffsetDateTime date;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "TIME_ELAPSED")
    private String elapsed;
    @Column(name = "HOME_TEAM")
    private String homeTeam;
    @Column(name = "HOME_LOGO")
    private String homeLogo;
    @Column(name = "HOME_WINNER")
    private boolean homeWinner;
    @Column(name = "AWAY_TEAM")
    private String awayTeam;
    @Column(name = "AWAY_LOGO")
    private String awayLogo;
    @Column(name = "AWAY_WINNER")
    private boolean awayWinner;
    @Column(name = "HOME_GOALS")
    private int homeGoals;
    @Column(name = "AWAY_GOALS")
    private int awayGoals;
}
