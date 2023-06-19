package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Entity
@Table(name = "GRADUATIONS")
public class Graduation {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @NotNull
    @Column(name = "POINTS")
    private int points ;
    @Column(name = "RANKS")
    private int rank;
    @Column(name = "LEAGUE_ID")
    private int leagueId;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Graduation(int league, User user) {
        this.points = 0;
        this.rank = 0;
        this.leagueId = league;
        this.user = user;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
