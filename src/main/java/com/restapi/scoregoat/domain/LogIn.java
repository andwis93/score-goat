package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Entity
@Table(name = "LOG_IN_ATTEMPTS")
public class LogIn {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "ATTEMPTS")
    private int attempt;
    @Column(name = "LOCKED_TILL")
    private LocalDateTime locked;
    @OneToOne(targetEntity=User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    public LogIn(User user) {
        this.attempt = 0;
        this.user = user;
    }

    public void addAttempt() {
        this.attempt++;
    }

    public void resetAttempt() {
        this.attempt = 0;
    }

    public void removeLocked() {
        this.locked = null;
        resetAttempt();
    }
}
