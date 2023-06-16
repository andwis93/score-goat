package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "CREATED")
    private LocalDate created;
    @OneToMany(
            targetEntity =  MatchPrediction.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<MatchPrediction> predictions = new HashSet<>();
    @OneToOne(mappedBy = "user", orphanRemoval=true)
    private LogIn logIn;
    @OneToOne(mappedBy = "user")
    private Session session;
    @OneToMany(
            targetEntity = Graduation.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<Graduation> graduation = new HashSet<>();


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = LocalDate.now();
    }

    public void addPrediction(MatchPrediction prediction) {
        this.predictions.add(prediction);
    }
}