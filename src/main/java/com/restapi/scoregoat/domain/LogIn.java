package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    public LogIn(User user) {
        this.attempt = 0;
        this.user = user;
    }

    public void addAttempt() {
        this.attempt += 1;
    }

    public void resetAttempt() {
        this.attempt = 0;
    }

    public void removeLocked() {
        this.locked = null;
        resetAttempt();
    }
}
