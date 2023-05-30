package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SESSION")
public class Session {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "SESSION_ENDS")
    private LocalDateTime end;
    @OneToOne(targetEntity=User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Session() {
        this.end = LocalDateTime.now().plusMinutes(DurationValues.SESSION_LENGTH.getValue());
    }
}
