package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
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
    @OneToMany(
            targetEntity =  MatchPrediction.class,
            mappedBy = "match",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<MatchPrediction> matchPredictions;

    public Match(@NotNull Long id, int leagueId, Long fixtureId, OffsetDateTime date, String status, String elapsed,
                 String homeTeam, String homeLogo, boolean homeWinner, String awayTeam, String awayLogo,
                 boolean awayWinner, int homeGoals, int awayGoals) {
        this.id = id;
        this.leagueId = leagueId;
        this.fixtureId = fixtureId;
        this.date = date;
        this.status = status;
        this.elapsed = elapsed;
        this.homeTeam = homeTeam;
        this.homeLogo = homeLogo;
        this.homeWinner = homeWinner;
        this.awayTeam = awayTeam;
        this.awayLogo = awayLogo;
        this.awayWinner = awayWinner;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.matchPredictions = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return leagueId == match.leagueId && id.equals(match.id) && Objects.equals(fixtureId, match.fixtureId) &&
                Objects.equals(date, match.date) && Objects.equals(homeTeam, match.homeTeam) &&
                Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leagueId, date);
    }
}


