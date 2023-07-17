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
@Table(name = "RANKING")
public class Ranking {
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
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    @Column(name = "LEAGUE_ID")
    private int leagueId;
    @Column(name = "STATUS")
    private int status;
    @Column(name ="1ST_PLACE_COUNTER")
    private int counter;
    @Column(name = "LAST_PLACE")
    private int last;
    public Ranking(User user, int league) {
        this.points = 0;
        this.rank = 0;
        this.user = user;
        this.leagueId = league;
        this.counter = 0;
        this.last = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }
    public void addToCounter() {
        this.counter++;
    }

}
