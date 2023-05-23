package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MATCHES_PREDICTION")
public class MatchPrediction {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "LEAGUE_ID")
    private int leagueId;
    @Column (name = "PREDICTIONS")
    private String prediction;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @Column(name = "FIXTURES_ID")
    private Long fixtureId;
    @Column(name = "POINTS")
    private int points;
    @Column(name = "RESULTS")
    private int result;
}
