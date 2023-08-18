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
@Table(name = "ACTIVE")
public class Active {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @NotNull
    @Column(name = "ACTIVITY")
    private Boolean active;
    @NotNull
    @Column(name = "LEAGUE_ID")
    private Integer leagueId;
}
